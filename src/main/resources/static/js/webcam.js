document.addEventListener('DOMContentLoaded', (event) => {
const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const context = canvas.getContext('2d');
const captureButton = document.getElementById('capture');
const resultList = document.getElementById('resultList');
const fruitMap ={
0 : '사과',
1 : '바나나',
2 : '오렌지',
}

// 웹캠 스트림 가져오기
navigator.mediaDevices.getUserMedia({ video: true })
.then((stream) => {
console.log('Successfully accessed webcam stream.');
video.srcObject = stream;
})
.catch((err) => {
console.error('Error accessing webcam: ' + err);
alert('Error accessing webcam: ' + err.message);
});

// WebSocket 연결
let socket = null;
let resultCount = 0; // 결과 번호를 유지하는 변수

function connect() {
socket = new WebSocket('ws://localhost:8080/ws');
socket.onopen = function () {
console.log('WebSocket connection established');
};
socket.onmessage = function (event) {
console.log('Received message:', event.data);
try {
const data = JSON.parse(event.data);
if (Array.isArray(data)) {
data.forEach(result => processResult(result));
} else {
processResult(data);
}
} catch (e) {
console.error('Error parsing WebSocket message:', e);
}
};
socket.onclose = function () {
console.log('WebSocket connection closed');
};
}

connect();


// 캡처 버튼 클릭 이벤트 처리
captureButton.addEventListener('click', () => {
// 캔버스 크기 설정
canvas.width = video.videoWidth;
canvas.height = video.videoHeight;

// 비디오에서 캔버스로 이미지 복사
context.drawImage(video, 0, 0, canvas.width, canvas.height);
const dataURL = canvas.toDataURL('image/jpeg');

fetch('http://localhost:8080/api/predict', {  // HTTP로 요청을 보냄
method: 'POST',
headers: {
'Content-Type': 'application/json'
},
body: JSON.stringify({ image: dataURL })
})
.then(response => response.json())
.then(data => {
if (data.code === 200) {
console.log('Success:', data);
displayResults(data.data);
canvas.style.display = 'block';  // 캔버스 표시
video.style.display = 'none';    // 비디오 숨김

// 2초 후에 캔버스를 지우고 비디오 다시 표시
setTimeout(() => {
context.clearRect(0, 0, canvas.width, canvas.height);
canvas.style.display = 'none'; // 캔버스 숨김
video.style.display = 'block'; // 비디오 표시
}, 4000);
} else {
console.error('Error:', data.msg);
alert('Error: ' + data.msg);
}
})
.catch((error) => {
console.error('Error:', error);
});
});

function displayResults(results) {
// 캔버스 초기화
context.clearRect(0, 0, canvas.width, canvas.height);
context.drawImage(video, 0, 0, canvas.width, canvas.height);

results.forEach(result => {
const { box, confidence, class: className } = result;
console.log(`Box: ${box}, Confidence: ${confidence}, Class: ${className}`);
drawBoundingBox(box, confidence, className);
});
}

function drawBoundingBox(box, confidence, className) {
const classNameStr = fruitMap[className] || className;
console.log('Drawing box:', box);
context.strokeStyle = 'red';
context.lineWidth = 3;
context.strokeRect(box[0], box[1], box[2] - box[0], box[3] - box[1]);
context.fillStyle = 'black';
context.font = '20px Arial';
context.fillText(`종류: ${classNameStr}, 정확도: ${confidence.toFixed(2)}`, box[0], box[1] > 10 ? box[1] - 5 : 10);
}

function processResult(result) {
if (result && result.id && result.imageUrl && result.objectName) {
addResultToList(result);
} else {
console.error('Invalid result object:', result);
}
}


function addResultToList(result) {
const listItem = document.createElement('li');
listItem.className = 'result-item';
const link = document.createElement('a');
link.className = 'result-link';
link.href = `/api/results/${result.id}`;
link.textContent = `새로운 내용입니다`;
listItem.appendChild(link);
resultList.appendChild(listItem);

}
});
