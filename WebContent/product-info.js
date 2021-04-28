 function handleResult(resultDataString) {
     console.log("Hello");
     console.log(resultDataString);
 }
//
//     $("#productName").val(resultDataString[name]);

//let id = resultDataString[id];
/*let url = new URL(window.location.href);
let product_id = parseInt(url.searchParams.get("product_id"));

let product = products.find((p) => p.id === product_id);

let productNameBody = jQuery("#productName");
let orderBtnBody = jQuery(".order-btn");
let infoBody = jQuery(".info");

productNameBody.append(`<i>${product.name}</i>`);

for (let imageSrc of product.image) {
  infoBody.append(`<img src=${imageSrc}>`);
}

infoBody.append(
    `<p>Quantity left available: <strong>${product.qty}</strong></p>`
);
infoBody.append(`<p>Price: <strong>${product.price}</strong></p>`);
infoBody.append(
    `<p>Manufactured by: <strong>${product.manufacturer}</strong></p>`
);
infoBody.append(`<p>- <i>${product.description}</i></p>`);

orderBtnBody.append(`<form action="order-page.html?product_id=${product_id}" method="POST">
<button type="submit">Order this product</button>
</form>`);*/
//}



$.ajax("product-info", {
    method: "GET",
        success: (resultData) => handleResult(resultData)
})
function onHamburgerMenuClick() {
  $('.navbar-items-wrapper').toggleClass('navbar-items-wrapper--responsive');
 }