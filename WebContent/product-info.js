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
    let orderBtnBody = $(".order-btn");


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

    orderBtnBody.append(`<button type="submit">Order this product</button>`);
}


let productId = getParameterByName('product_id');

$.ajax("product-info?product_id=" + productId, {
    method: "GET",
        success: (resultData) => handleResult(resultData)
})
function onHamburgerMenuClick() {
  $('.navbar-items-wrapper').toggleClass('navbar-items-wrapper--responsive');
 }