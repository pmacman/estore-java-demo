import $ from "jquery";
import axios from "axios";

function init() {
    filterStates($("#country").val());

    $("#country").change(function() {
        filterStates($(this).val());
    });
}

function filterStates(country) {
    if (country === "")
        return;

    axios.get("/locale/states", {
        params: {
            country: country
        }
    }).then(function(response) {
        $("#state").html(response.data);

        const stateId = $("#stateId").val();
        if (stateId !== "") {
            $("#state").val(stateId);
        }
    }).catch(function(error) {
        console.log(error);
    });
}

export let LocaleModule = {
    init: init
};