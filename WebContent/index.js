function handleResult(resultDataString){
  let productBodyElement = jQuery(".Product");
  console.log('resultDataString', resultDataString);
  let productHtml = '<div class="product-container">';
  for (let product of resultDataString[0]) {
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

  if (resultDataString[1]) {
    handleRecentlyBoughtItemsData(resultDataString);
  }
}

$.ajax("all-products", {
      method: "GET",
      success: (resultData) => handleResult(resultData)
    }
)

function onHamburgerMenuClick() {
  $(".navbar-items-wrapper").toggleClass("navbar-items-wrapper--responsive");
}

const row = `
    <div class="row">
        <div class="col-sm">:order_id</div>
        <div class="col-sm">:product_name</div>
        <div class="col-sm">:unit_price</div>
        <div class="col-sm">:select_amount</div>
        <div class="col-sm">:ratings</div>
<!--        <div class="col-sm">:created_at</div>-->
<!--        <div class="col-sm">:order_total_price</div>-->
<!--        <div class="col-sm">:shipping_address</div>-->
<!--        <div class="col-sm">:shipping_method</div>-->
    </div>
`;

function handleRecentlyBoughtItemsData(resultDataString) {
  // let productHtml = '<div class="container">';
  let appendedElem = '';
  let index = 1;
  for (let product of resultDataString[1]) {
    let newRow = row;
    newRow = newRow.replace(':order_id', product.order_id);
    newRow = newRow.replace(':product_name', product.name);
    newRow = newRow.replace(':unit_price', product.price);
    newRow = newRow.replace(':select_amount', product.selected_amount);
    newRow = newRow.replace(':ratings', '<input type="range" value="1" min="1" max="5" oninput="this.nextElementSibling.value = this.value">\n' +
        '<output>1</output>');
    // newRow = newRow.replace(':created_at', product.created_at);
    // newRow = newRow.replace(':order_total_price', product.order_total_price);
    // newRow = newRow.replace(':shipping_address', product.order_shipping_address);
    // newRow = newRow.replace(':shipping_method', product.order_shipping_method);
    index ++;
    appendedElem = `${appendedElem} \n ${newRow}`;
  }

  $('.order-user-container').append(appendedElem)
}
