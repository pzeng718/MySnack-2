public class Product {
    private int productID;
    private String productName;
    private double productPrice;
    private int productQty;
    private String productDescription;
    private String productManufacturer;
    private String productImages;

    public Product(int productID, String productName, double productPrice, int productQty, String productDescription, String productManufacturer, String productImages) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productDescription = productDescription;
        this.productManufacturer = productManufacturer;
        this.productImages = productImages;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public void setProductManufacturer(String productManufacturer) {
        this.productManufacturer = productManufacturer;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }
}
