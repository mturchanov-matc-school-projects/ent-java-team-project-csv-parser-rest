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
* Hides all fields that can depend on the query type.
*/
const hideAllVariableFields = () => {
    let queryColumnGroup = document.querySelector("#queryColumnGroup");
    queryColumnGroup.hidden = true;
}

/*
* Updates fields to be hidden or shown based on the provided query type.
*/
const updateHiddenFields = queryType => {
    let queryForm = document.querySelector("#queryForm");
    let queryColumnGroup = document.querySelector("#queryColumnGroup");
    hideAllVariableFields();
    switch (queryType) {
        case "count":
            queryForm.setAttribute("action", "rest/jsonqueryservice/count");
            break;
        case "search":
        default:
            queryForm.setAttribute("action", "rest/jsonqueryservice/search");
            queryColumnGroup.hidden = false;
            break;
    }
}

/*
* Update fields that hide or show themselves based on query type when the type is changed.
*/
const queryTypeChanged = event => {
    updateHiddenFields(event.target.value);
}

/*
* Initialize form styling event listeners and variable display input groups
*/
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