const fileInputChanged = event => {
    let element = event.target;
    let label = document.querySelector("#fileInputLabel");
    let fileName = element.files[0].name;
    label.textContent = fileName;
}

const formStyleInit = () => {
    let fileInputElement = document.querySelector("#fileInput");
    if (fileInputElement) fileInputElement.addEventListener("change", fileInputChanged);
}

window.addEventListener("load", formStyleInit);