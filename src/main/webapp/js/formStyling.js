const fileInputChanged = event => {
    let element = event.target;
    let label = document.querySelector("#fileInputLabel");
    let fileName = element.files[0].name;
    label.textContent = fileName;
}

const hideAllVariableFields = () => {
    let queryColumnGroup = document.querySelector("#queryColumnGroup");
    queryColumnGroup.hidden = true;
}

const updateHiddenFields = queryType => {
    let queryColumnGroup = document.querySelector("#queryColumnGroup");
    hideAllVariableFields();
    switch (queryType) {
        case "value":
            queryColumnGroup.hidden = false;
            break;
        case "all":
        default:
            break;
    }
}

const queryTypeChanged = event => {
    updateHiddenFields(event.target.value);
}

const formStyleInit = () => {
    let fileInputElement = document.querySelector("#fileInput");
    if (fileInputElement) fileInputElement.addEventListener("change", fileInputChanged);

    let querySelectElement = document.querySelector("#queryTypeInput");
    if (querySelectElement) {
        querySelectElement.addEventListener("change", queryTypeChanged);
        updateHiddenFields(querySelectElement.value);
    }
}

window.addEventListener("load", formStyleInit);