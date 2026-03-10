import $ from "jquery";
import axios from "axios";
import toastr from "toastr";
import "toastr/build/toastr.min.css";

function init() {
    // Loads add product form. This is called when Select button is pressed in the DataTable.
    $("body").on("click", "#addProductBtn", function() {
        axios.get("/partner/product").then(function(response) {
            $("#productDetailsSection").html(response.data);
        }).catch(function(error) {
            console.log(error);
        });
    });

    // Loads product edit form. This is called when Select button is pressed in the DataTable.
    $("body").on("click", ".js-product-btn", function() {
        const productId = $(this).data("productid");
        axios.get("/partner/product/" + productId).then(function(response) {
            $("#productDetailsSection").html(response.data);
        }).catch(function(error) {
            console.log(error);
        });
    });

    $("body").on("submit", "#productForm", function(e) {
        e.preventDefault();
        const $form = $(this);

        axios({
            method: "post",
            url: $form.attr("action"),
            data: $form.serialize(),
            headers: { "Content-Type": "application/x-www-form-urlencoded" }
        }).then(function(response) {
            $("#productModal").modal("hide");
            const table = $("#productDataTable").DataTable();
            table.draw();
        }).catch(function(error) {
            console.log(error);
        });
    });

    $("#partnerProfileForm").submit(function(e) {
        e.preventDefault();
        const $form = $(this);

        axios({
            method: "post",
            url: $form.attr("action"),
            data: $form.serialize(),
            headers: { "Content-Type": "application/x-www-form-urlencoded" }
        }).then(function(response) {
            toastr.success("Your profile has been saved.");
        }).catch(function(error) {
            console.log(error);
        });
    });
}

export let PartnerModule = {
    init: init
};