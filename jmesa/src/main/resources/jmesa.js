
/*********** Convenience API Methods ***********/

function addTableFacadeToManager(id) {
    jQuery.jmesa.addTableFacade(id);
}

function setSaveToWorksheet(id) {
    jQuery.jmesa.setSaveToWorksheet(id);
}

function setFilterToWorksheet(id) {
    jQuery.jmesa.setFilterToWorksheet(id);
}

function removeFilterFromWorksheet(id) {
    jQuery.jmesa.removeFilterFromWorksheet(id);
}

function setPageToLimit(id, page) {
    jQuery.jmesa.setPage(id, page);
}

function setMaxRowsToLimit(id, maxRows) {
    jQuery.jmesa.setMaxRows(id, maxRows);
}

function addSortToLimit(id, position, property, order) {
    jQuery.jmesa.setSort(id, position, property, order);
}

function removeSortFromLimit(id, property) {
    jQuery.jmesa.removeSortFromLimit(id, property);
}

function removeAllSortsFromLimit(id) {
    jQuery.jmesa.removeAllSortsFromLimit(id);
}

function getSortFromLimit(id, property) {
    jQuery.jmesa.getSortFromLimit(id, property);
}

function addFilterToLimit(id, property) {
    jQuery.jmesa.setFilterToWorksheet(id, property);
}

function removeFilterFromLimit(id, property) {
    jQuery.jmesa.removeFilterFromWorksheet(id, property);
}

function removeAllFiltersFromLimit(id) {
    jQuery.jmesa.removeFilterFromWorksheet(id);
}

function getFilterFromLimit(id, property) {
    jQuery.jmesa.getFilterFromLimit(id, property);
}

function setExportToLimit(id, exportType) {
    jQuery.jmesa.setExport(id, exportType);
}

function createHiddenInputFieldsForLimit(id) {
    jQuery.jmesa.createHiddenInputFieldsForLimit(id);
}

function createHiddenInputFieldsForLimitAndSubmit(id) {
    jQuery.jmesa.createHiddenInputFieldsForLimitAndSubmit(id);
}

function createParameterStringForLimit(id) {
    return jQuery.jmesa.createParameterStringForLimit(id);
}

/*********** Filter ***********/

function createDynFilter(filter, id, property) {
    jQuery.jmesa.createDynFilter(filter, id, property);
}

function createDynDroplistFilter(filter, id, property, options) {
    jQuery.jmesa.createDroplistDynFilter(filter, id, property, options);
}

/*********** Worksheet ***********/

function createWsColumn(column, id, uniqueProperties, property) {
    jQuery.jmesa.createWsColumn(column, id, uniqueProperties, property);
}

function submitWsCheckboxColumn(column, id, uniqueProperties, property) {
    jQuery.jmesa.submitWsCheckboxColumn(column, id, uniqueProperties, property);
}

function submitWsColumn(originalValue, changedValue) {
    jQuery.jmesa.submitWsColumn(originalValue, changedValue);
}

/*********** Special Effects ***********/

function addDropShadow(imagesPath, theme) {
    jQuery.jmesa.addDropShadow(imagesPath, theme);
}