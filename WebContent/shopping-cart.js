function EmptyCart(){
    $.ajax("shopping-cart", {
        method: "POST",
        data: "action=emptyCart",
        success: ()=>{
            alert("Cart is now empty");
            location.reload(true);
        }
    })
}

function handleShowShoppingCart(resultData){
    let resultDataJson = JSON.parse(resultData);

    let shoppingCartBody = $(".shopping-cart");
    let items = resultDataJson.items;
    let itemHTML = '<table class="table">' +
        '<thead>' +
            '<tr>' +
            '<th scope="col">#</th>' +
            '<th scope="col">Product ID</th>' +
            '<th scope="col">Name</th>' +
            '<th scope="col">Qty</th>' +
            '<th scope="col">Unit Price</th>' +
            '</tr>\n' +
        '</thead><tbody>';
    let index = 1;
    let totalPrice = 0;
    if(resultDataJson.itemExists) {
        for (let productId in items) {
            itemHTML += `<tr>` +
                `<th scope="row">${index}</th>` +
                `<td>${productId}</td>` +
                `<td>${items[productId][0]}</td>` +
                `<td>${items[productId][2]}</td>` +
                `<td>$${items[productId][1]}</td>` +
                `</tr>`;
            totalPrice += parseInt(items[productId][2]) * parseFloat(items[productId][1]);
            index++;
        }

        let emptyCartButtonBody = $("#empty-cart-btn");
        emptyCartButtonBody.empty();
        emptyCartButtonBody.append("<button class='btn btn-danger' onclick='EmptyCart()'>Empty Cart</button>")
    }else{
        let cartEmptyMsgBody = $(".cart-empty-msg");
        cartEmptyMsgBody.empty();
        cartEmptyMsgBody.append("Your shopping cart is empty right now!")
    }

    itemHTML += '</tbody></table>';
    itemHTML += `<h4 style="text-align: right">Total Price: $<span id="total-price">${(totalPrice + 9.99).toFixed(2)}</span></h4>`
    itemHTML += `<input type="hidden" value="${totalPrice.toFixed(2)}" id="product-total-price">`;
    shoppingCartBody.append(itemHTML);

    let submitButtonBody = $(".submit-button");
    let disabled = (resultDataJson.itemExists) ? '': 'disabled';
    let submitButtonHTML = '<div style="position: relative" class="mt-5">\n' +
        `        <button type="submit" class="btn btn-success" ${disabled}` +
        '                style="margin: 0; position: absolute;left: 50%;-ms-transform: translateX(-50%);transform: translateX(-50%);">' +
        '            Confirm Order' +
        '        </button>' +
        '    </div>';

    submitButtonBody.append(submitButtonHTML);
}

$.ajax("shopping-cart", {
    method: "GET",
    success: (resultData) => handleShowShoppingCart(resultData)
})