import sys
from pathlib import Path
from flask import Flask, request, jsonify
import torch
import cv2
import numpy as np
from models.experimental import attempt_load
from utils.general import check_img_size, non_max_suppression, scale_boxes, set_logging, strip_optimizer
from utils.torch_utils import select_device
import time

app = Flask(__name__)

# 현재 파일이 위치한 경로
FILE = Path(__file__).resolve()
# 부모 디렉토리로 이동하여 yolov5 디렉토리를 찾음
ROOT = FILE.parents[0]

if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))  # yolov5 디렉토리를 PYTHONPATH에 추가

# yolov5 내의 다른 디렉토리도 필요하면 추가
ROOT = ROOT.relative_to(Path.cwd())  # 경로를 상대 경로로 변환

# 모델 로드 함수
def load_model(weights, device):
    try:
        model = attempt_load(weights)  # load FP32 model
        model.to(device)
        return model
    except Exception as e:
        print(f"Error loading model: {e}")
        return None

# 예측 함수
def detect(image, model, device, img_size, conf_thres):
    img0 = image
    img = cv2.resize(img0, (img_size, img_size))  # Resize image to 640x640
    img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, to 3x640x640
    img = np.ascontiguousarray(img)

    img = torch.from_numpy(img).to(device)
    img = img.float()  # uint8 to fp16/32
    img /= 255.0  # 0 - 255 to 0.0 - 1.0
    if img.ndimension() == 3:
        img = img.unsqueeze(0)

    # Inference
    t1 = time.time()
    pred = model(img, augment=False)[0]
    t2 = time.time()
    print(f"Inference time: {t2 - t1}")

    # Apply NMS
    pred = non_max_suppression(pred, conf_thres, 0.45, classes=None, agnostic=False)

    return pred

# YOLOv5 모델 로드
device = select_device('')
model = load_model('runs/train/fruitdataset_yolov5s_results4/weights/best.pt', device)
img_size = 640  # Image size
conf_thres = 0.5  # Confidence threshold

if not model:
    print("Failed to load model. Exiting...")
    sys.exit(1)

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'})

    file = request.files['file']

    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    if file:
        img_bytes = file.read()
        nparr = np.frombuffer(img_bytes, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        if img is None:
            return jsonify({'error': 'Image decoding failed'})

        # 예측 수행
        pred = detect(img, model, device, img_size, conf_thres)

        # 결과 처리 및 반환
        results = []
        for det in pred:
            if len(det):
                det[:, :4] = scale_boxes(img.shape[2:], det[:, :4], img.shape).round()
                for *xyxy, conf, cls in det:
                    result = {
                        'box': [int(coord) for coord in xyxy],
                        'confidence': float(conf),
                        'class': int(cls)
                    }
                    results.append(result)

        return jsonify(results)

if __name__ == '__main__':
    app.run(debug=True)
