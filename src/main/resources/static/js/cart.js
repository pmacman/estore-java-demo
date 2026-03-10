import $ from "jquery";
import axios from "axios";

function init() {
    $(".js-remove-item-btn").click(function() {
        const productId = $(this).val();
        const $form = $(this).closest("form");
        const url = $form.attr("action");

        axios.delete(url, {
            params: {
                productId: productId
            }
        }).then(function(response) {
            window.load();
        });
    });
}

export let CartModule = {
    init: init
};