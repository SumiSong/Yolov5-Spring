
    const canvas = document.getElementById('canvas');
    const context = canvas.getContext('2d');
    let drawing = false;

    // 기본 브러쉬 설정
    context.strokeStyle = '#2c2c2c';
    context.lineWidth = 5;

    // Mouse events for drawing on the canvas
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

    // Clear button event
    document.getElementById('clear').addEventListener('click', () => {
    context.clearRect(0, 0, canvas.width, canvas.height);
});

    // Brush width change event
    document.getElementById('line-width').addEventListener('input', (e) => {
    context.lineWidth = e.target.value;
});

    // Custom color change event
    document.getElementById('color').addEventListener('input', (e) => {
    context.strokeStyle = e.target.value;
});

    // Color option click events
    const colors = document.getElementsByClassName('color-option');
    Array.from(colors).forEach(color => {
    color.addEventListener('click', (e) => {
        const selectedColor = e.target.getAttribute('data-color');
        context.strokeStyle = selectedColor;
    });
});

    // Detect button event
    document.getElementById('detect').addEventListener('click', () => {
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
    console.log('Detection Result:', data);
    // Display detection results (e.g., bounding boxes, labels) on the canvas
})
    .catch((error) => {
    console.error('Error:', error);
});
});
