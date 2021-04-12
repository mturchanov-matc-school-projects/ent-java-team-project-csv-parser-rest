/*
* Updates the label associated with the provided for a
*/
const fileInputChanged = event => {
    let element = event.target;
    let label = document.querySelector("#fileInputLabel");
    let fileNameInput = document.querySelector("#fileNameInput");
    let fileName = element.files[0].name;
    label.textContent = fileName;
    fileNameInput.value = fileName;
}

/*
* Changes JSON query form action to count if the count checkbox has been checked
*/
const updateFormEndpoint = event => {
    let element = event.target;
    let queryForm = document.querySelector("#queryForm");
    if (element.checked) {
        queryForm.setAttribute("action", "rest/jsonqueryservice/count");
    } else {
        queryForm.setAttribute("action", "rest/jsonqueryservice/search");
    }
}

/*
* Changes JSON query form action to count if the count checkbox has been checked
*/
const updateCSVInputType = event => {
    let element = event.target;
    let csvFileInputGroup = document.querySelector("#csvFileInputGroup");
    let csvFileInput = document.querySelector("#fileInput");
    let csvFileNameInput = document.querySelector("#fileNameInput");
    let rawCSVInputGroup = document.querySelector("#rawCSVInputGroup");
    let rawCSVInput = document.querySelector("#csvTextInput");
    if (element.checked) {
        csvFileInputGroup.hidden = true;
        csvFileInput.disabled = true;
        csvFileNameInput.disabled = true;
        rawCSVInputGroup.hidden = false;
        rawCSVInput.disabled = false;
    } else {
        csvFileInputGroup.hidden = false;
        csvFileInput.disabled = false;
        csvFileNameInput.disabled = false;
        rawCSVInputGroup.hidden = true;
        rawCSVInput.disabled = true;
    }
}

/*
* Initialize dynamic form styling event listeners.
*/
const formStyleInit = () => {
    let fileInputElement = document.querySelector("#fileInput");
    if (fileInputElement) fileInputElement.addEventListener("change", fileInputChanged);

    let countResultsInput = document.querySelector("#countResults");
    if (countResultsInput) {
        countResultsInput.addEventListener("change", updateFormEndpoint);
    }

    let pasteCSVInput = document.querySelector("#pasteCSVInput");
    if (pasteCSVInput) {
        pasteCSVInput.addEventListener("change", updateCSVInputType);
    }
}



window.addEventListener("load", formStyleInit);