
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ProductInfoServlet", urlPatterns = "/product-info")
public class ProductInfoServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mysnack");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        String productID = request.getParameter("product_id"); //get the id from the url
        System.out.println(productID);
        try{
            Connection con = dataSource.getConnection();
            String productQuery = "select p.id, p.name, p.price, p.qty, p.description, p.manufacturer, group_concat(imgSrc) as images " +
                    "from products as p, images as i where p.id = i.product_id and p.id = ?;";

            PreparedStatement ps = con.prepareStatement(productQuery);
            ps.setString(1, productID);
            ResultSet result = ps.executeQuery();
            JsonObject jsonProductObject = new JsonObject();
            if(result.next()) {
                String id = result.getString("id");
                String name = result.getString("name");
                String price = result.getString("price");
                String qty = result.getString("qty");
                String description = result.getString("description");
                String manufacturer = result.getString("manufacturer");
                String images = result.getString("images");

                jsonProductObject.addProperty("id", id);
                jsonProductObject.addProperty("name", name);
                jsonProductObject.addProperty("price", price);
                jsonProductObject.addProperty("qty", qty);
                jsonProductObject.addProperty("description", description);
                jsonProductObject.addProperty("manufacturer", manufacturer);
                jsonProductObject.addProperty("images", images);
           }

            out.write(jsonProductObject.toString());
//            RequestDispatcher dispatcher = request.getRequestDispatcher("product-info.html");
//            request.setAttribute("productName", result.getString("name")); // set your String value in the attribute
//            dispatcher.forward( request, response );
            response.setStatus(200);

            result.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            JsonObject jsonProductObject = new JsonObject();
            jsonProductObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonProductObject.toString());
            System.out.println(e.getMessage());
            System.out.println(e);
            response.setStatus(500);



        } finally {
            out.close();
        }
    }
}
