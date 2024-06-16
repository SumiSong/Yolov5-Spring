document.addEventListener('DOMContentLoaded', (event) => {
    const canvas = document.getElementById('canvas');
    const context = canvas.getContext('2d');
    const captureButton = document.getElementById('capture');
    const clearButton = document.getElementById('clear');
    const lineWidthInput = document.getElementById('line-width');
    const colorInput = document.getElementById('color');
    const resultList = document.getElementById('resultList');
    const fruitMap = {
        0: '사과',
        1: '바나나',
        2: '오렌지',
    };
    let drawing = false;

    // 그림 그리기 이벤트 처리
    canvas.addEventListener('mousedown', (e) => {
        drawing = true;
        context.beginPath();
        context.moveTo(e.offsetX, e.offsetY);
    });

    canvas.addEventListener('mousemove', (e) => {
        if (drawing) {
            context.lineTo(e.offsetX, e.offsetY);
            context.stroke();
        }
    });

    canvas.addEventListener('mouseup', () => {
        drawing = false;
        context.closePath();
    });

    // Clear 버튼 이벤트 처리
    clearButton.addEventListener('click', () => {
        context.clearRect(0, 0, canvas.width, canvas.height);
    });

    // Brush width 변경 이벤트 처리
    lineWidthInput.addEventListener('input', (e) => {
        context.lineWidth = e.target.value;
    });

    // Custom color 변경 이벤트 처리
    colorInput.addEventListener('input', (e) => {
        context.strokeStyle = e.target.value;
    });

    // Color option 클릭 이벤트 처리
    const colors = document.getElementsByClassName('color-option');
    Array.from(colors).forEach(color => {
        color.addEventListener('click', (e) => {
            const selectedColor = e.target.getAttribute('data-color');
            context.strokeStyle = selectedColor;
            colorInput.value = selectedColor; // 컬러 인풋 업데이트
        });
    });

    // 캡처 버튼 클릭 이벤트 처리
    captureButton.addEventListener('click', () => {
        const dataURL = canvas.toDataURL('image/jpeg');

        fetch('http://localhost:8080/api/draw/predict', {
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
        if (result && result.id && result.drawimageUrl  && result.drawobjectName !== undefined) {
            console.log(`Original objectName: ${result.drawobjectName}`);
            const classNameStr = fruitMap[result.drawobjectName] || result.drawobjectName;
            console.log(`Mapped objectName: ${classNameStr}`);
            result.objectName = classNameStr;
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
        link.href = `/api/draw/results/${result.id}`;
        link.textContent = `새로운 내용입니다`;
        listItem.appendChild(link);
        resultList.insertBefore(listItem, resultList.firstChild);
    }

    // WebSocket 연결
    let socket = null;
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
});
