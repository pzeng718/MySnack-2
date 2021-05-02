function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleResult(resultDataString) {
    console.log("Hello");
    console.log(resultDataString);
    let item_name = resultDataString.name;
    let item_price = resultDataString.price;
    let item_description = resultDataString.description;
    let item_quantity = resultDataString.qty;
    let item_mnf = resultDataString.manufacturer;
    let item_imgs = resultDataString.images.split(",");

    let productNameBody = $("#productName");
    let infoBody = $(".info");
    let addToCartBody = $(".add-to-cart");

    productNameBody.append(`<i>${item_name}</i>`);

    infoBody.append(`<img src=${item_imgs[0]}>`);
    infoBody.append(`<img src=${item_imgs[1]}>`);

    infoBody.append(
        `<p>Quantity left available: <strong>${item_quantity}</strong></p>`
    );
    infoBody.append(`<p>Price: <strong>${item_price}</strong></p>`);
    infoBody.append(
        `<p>Manufactured by: <strong>${item_mnf}</strong></p>`
    );
    infoBody.append(`<p>- <i>${item_description}</i></p>`);

    let addToCartHTML = `<form action='#' id="add-to-cart-form">` +
        `<input value=${resultDataString.id} name='productId' type="hidden">` +
        `<input value=${resultDataString.price} name='price' type="hidden">` +
        `<input value=${resultDataString.name} name='name' type="hidden">` +
        '<select class="form-select" name="qtySelected" id="inputGroupSelect04" aria-label="Example select with button addon" style="width: 150px; display: inline">';
    for(let qty = 1; qty <= parseInt(item_quantity); qty++){
        addToCartHTML += `<option value=${qty}>${qty}</option>`;
    }
    addToCartHTML += '</select>' +
    `<button type="submit" style="display: inline">Add to Cart</button>` +
    `</form>`;

    addToCartBody.append(addToCartHTML);
    let addToCartFormBody = $("#add-to-cart-form");
    addToCartFormBody.submit(cartEvent => {
        cartEvent.preventDefault();
        console.log(addToCartFormBody.serialize());

        $.ajax("shopping-cart", {
            method: "POST",
            data: addToCartFormBody.serialize(),
            success: (resultData) =>{
                console.log(resultData)
                alert("Item added successfully")
            }
        })
    })
}


let productId = getParameterByName('product_id');

$.ajax("product-info?product_id=" + productId, {
    method: "GET",
        success: (resultData) => handleResult(resultData)
})
function onHamburgerMenuClick() {
  $('.navbar-items-wrapper').toggleClass('navbar-items-wrapper--responsive');
 }