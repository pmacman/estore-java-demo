import $ from "jquery";

function init() {
    const $productSearchForm = $("#productSearchForm");
    const $dataTable = $("#productDataTable");

    // Loads search results.
    $productSearchForm.submit(function(e) {
        e.preventDefault();

        const table = $dataTable.DataTable();
        table.draw();
    });

    // Init DataTable
    $dataTable.DataTable({
        ajax: {
            url: $productSearchForm.attr("action"),
            type: $productSearchForm.attr("method"),
            data: function(d) {
                d.productName = $("#productName").val();
            }
        },
        ordering: false,
        pageLength: 10,
        processing: true,
        scrollX: true,
        searching: false,
        serverSide: true,
        deferLoading: 0,
        dom: "<'top'>rt<'bottom'pil><'clear'>",
        columns: [
            { data: "id" },
            { data: "name" },
            { data: "price" },
            { data: "quantity" },
            { data: "status" }
        ],
        columnDefs: [
            {
                targets: 0,
                width: "75px",
                render: function(data, type, row, meta) {
                    return "<button type='button' class='btn btn-outline-primary btn-sm js-product-btn' data-productid='" + data +
                        "' data-toggle='modal' data-target='#productModal'>Select</button>";
                }
            },
            {
                targets: 2,
                render: function(data, type, row, meta) {
                    return "$" + data;
                }
            }
        ]
    });

    const table = $dataTable.DataTable();
    table.draw();
}

export let SearchModule = {
    init: init
};