/*
* Updates the label associated with the provided for a
*/
const fileInputChanged = event => {
    let element = event.target;
    let label = document.querySelector("#fileInputLabel");
    let fileName = element.files[0].name;
    label.textContent = fileName;
}

/*
* Changes JSON query form action to count if the count checkbox has been checked
*/
const countResultsChanged = event => {
    let element = event.target;

    let queryForm = document.querySelector("#queryForm");
    if (element.checked) {
        queryForm.setAttribute("action", "rest/jsonqueryservice/count");
    } else {
        queryForm.setAttribute("action", "rest/jsonqueryservice/search");
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
        countResultsInput.addEventListener("change", countResultsChanged);
    }
}

window.addEventListener("load", formStyleInit);