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
    if(resultDataJson.itemExists){
        for(let productId in items){
            itemHTML += `<tr>` +
            `<th scope="row">${index}</th>` +
            `<td>${productId}</td>` +
            `<td>${items[productId][0]}</td>` +
            `<td>${items[productId][2]}</td>` +
            `<td>$${items[productId][1]}</td>` +
        `</tr>`;
            totalPrice += parseInt(items[productId][2]) * parseFloat(items[productId][1]);
            index ++;
        }
    }

    itemHTML += '</tbody></table>';
    itemHTML += `<h4 style="text-align: right">Total Price: $${totalPrice.toFixed(2)}</h4>`
    shoppingCartBody.append(itemHTML);
}

$.ajax("shopping-cart", {
    method: "GET",
    success: (resultData) => handleShowShoppingCart(resultData)
})