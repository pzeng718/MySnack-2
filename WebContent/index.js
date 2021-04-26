function handleResult(resultDataString){
  let productBodyElement = jQuery(".Product");

  let productHtml = '<div class="product-container">';
  for (let product of resultDataString) {
    productHtml += '<div class="product-item">';
    let images = product["images"].split(",");
    productHtml +=
        `<a href="product-info.html?product_id=${product['id']}"> <img class='product-image' src='${images[0]}' alt${product['name']}"></a>`;

    productHtml += '<div class="product-title">';

    productHtml += `<p>Name: <strong>${product['name']}</p></strong>`;
    productHtml += `<p>Price: <strong>${product['price']}</p></strong>`;
    productHtml += `<p>Quantity: <strong>${product['qty']}</p></strong>`;
    productHtml += "</div>";

    productHtml += "</div>";
  }
  productHtml += "</div>";
  productBodyElement.append(productHtml);
}

$.ajax("all-products", {
      method: "GET",
      success: (resultData) => handleResult(resultData)
    }
)

function onHamburgerMenuClick() {
  $(".navbar-items-wrapper").toggleClass("navbar-items-wrapper--responsive");
}