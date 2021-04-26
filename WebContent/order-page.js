import {products} from "./index.js";

let url = new URL(window.location.href);

let product_id = parseInt(url.searchParams.get("product_id"));
let product = products.filter((p) => p.id === product_id)[0] || {};

let productNameBody = jQuery(".product-name");
let productImageBody = jQuery(".product-image");
let amountSelectBody = jQuery("#amount-select");
let totalPriceBody = jQuery(".total-price");
let orderSummaryBody = jQuery(".order-summary");
let unitPriceBody = jQuery("#unit-price");

productNameBody.append(`${product.name}`);
if (product.image) {
  for (let imgSrc of product.image) {
    productImageBody.append(`<img src=${imgSrc} alt=${product.name}>`);
  }
}

let amountSelectHtml = `<input type="number" id="amount" value="1" min="1" max="${product.qty}" size="200px"> `;
amountSelectBody.append(amountSelectHtml);

let shippingCost = 9.99;
let qty = 1;

totalPriceBody.append(`$${(qty * product.price + shippingCost).toFixed(2)}`);

unitPriceBody.append(`$${product.price}`);
// Update total price based on the amount of products user select
jQuery("#amount").on("change", function () {
  var elem = $(this);
  qty = elem.val();
  elem
    .closest(".order-form")
    .find(".total-price")
    .text(`$${(qty * product.price + shippingCost).toFixed(2)}`);

  elem.closest(".order-form").find("#qty-selected").text(`${qty}`);
});

const shippingMethodsToPrice = {
  sameday: 9.99,
  overnight: 5.99,
  "2-day": 1.99,
  standard: 0,
};
// Add the shipping cost to the total price
jQuery("#shipping-method").on("change", function () {
  var elem = $(this);
  var shippingMethod = elem.val();
  var shippingCost = shippingMethodsToPrice[shippingMethod];
  elem
    .closest(".order-form")
    .find(".total-price")
    .text(`$${(qty * product.price + shippingCost).toFixed(2)}`);

  elem.closest(".order-form").find("#shipping-cost").text(`$${shippingCost}`);
});

// Nav bar responsive
function onHamburgerMenuClick() {
  $(".navbar-items-wrapper").toggleClass("navbar-items-wrapper--responsive");
}

// form validation
$(document).ready(function () {

  $("#order-form").validate({
    submitHandler: function (form) {
      // do other things for a valid form
      console.log(sendEmail());
    },
  });
});

// send email
function sendEmail() {
  let amountSelect = $("#order-form").find("#amount-select").val();
  amountSelect =
    amountSelect && parseInt(amountSelect) ? parseInt(amountSelect) : 1;
  const shippingMethod = $("#order-form").find("#shipping-method").val();
  const shippingPrice = shippingMethodsToPrice[shippingMethod];
  const totalPrice = product.price * amountSelect + shippingPrice;
  var recepientEmail = $("#order-form").find("#email").val();
  var subject = "MySnack Order Confirmation";
  var emailContent =
    "Hello, This is MySnacks shop %0D%0A" +
    "Here is your order: " +
    "%0D%0AItem: " +
    product.name +
    "%0D%0AQuantity: " +
    amountSelect +
    "%0D%0ATotal Price: " +
    totalPrice +
    "%0D%0AThank you for shopping with us.";
  window.location =
    "mailto:" +
    recepientEmail +
    "?subject=" +
    subject +
    "&body=" +
    emailContent;
  return emailContent;
  
}
