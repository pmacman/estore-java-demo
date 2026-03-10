import $ from "jquery";
import axios from "axios";
import toastr from "toastr";
import "toastr/build/toastr.min.css";

function init() {
    // Loads product details. This is called when Select button is pressed in the DataTable.
    $("body").on("click", ".js-product-btn", function() {
        const productId = $(this).data("productid");
        axios.get("/product/" + productId).then(function(response) {
            $("#productDetailsSection").html(response.data);
        }).catch(function(error) {
            location.href = "/customer";
            console.log(error);
        });
    });

    $("body").on("submit", "#addToCartForm", function(e) {
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

    $("#customerProfileForm").submit(function(e) {
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

export let CustomerModule = {
    init: init
};