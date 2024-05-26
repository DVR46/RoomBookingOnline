$(document).ready(function() {
    $(".sampleTable").fancyTable({
        sortColumn: 3,
        pagination: true,
        perPage: 5,
        globalSearch:true,
        globalSearchExcludeColumns: [7],
        paginationClass:"btn btn-outline-secondary p-2",
        paginationElement: '#paging-element',

    });
});