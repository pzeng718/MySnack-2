const row = `
    <div class="row">
        <div class="col-sm">:product_name</div>
        <div class="col-sm">:unit_price</div>
        <div class="col-sm">:select_amount</div>
        <div class="col-sm">:created_at</div>
        <div class="col-sm">:order_total_price</div>
        <div class="col-sm">:shipping_address</div>
        <div class="col-sm">:shipping_method</div>
    </div>
`;

function handleResult(resultDataString) {
    // let productHtml = '<div class="container">';
    let appendedElem = '';
    for (let product of resultDataString) {
        let newRow = row;
        newRow = newRow.replace(':product_name', product.name);
        newRow = newRow.replace(':unit_price', product.price);
        newRow = newRow.replace(':select_amount', product.selected_amount);
        newRow = newRow.replace(':created_at', product.created_at);
        newRow = newRow.replace(':order_total_price', product.order_total_price);
        newRow = newRow.replace(':shipping_address', product.order_shipping_address);
        newRow = newRow.replace(':shipping_method', product.order_shipping_method);
        appendedElem = `${appendedElem} \n ${newRow}`;
    }

    $('.order-user-container').append(appendedElem)
}

let orderId = 1;
$(document).ready(() => {
    $.ajax(`order-user?order_id=${orderId}`, {
        method: "GET",
        success: (resultData) => handleResult(resultData)
    })
});
