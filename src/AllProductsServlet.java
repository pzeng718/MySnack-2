import com.google.gson.JsonArray;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AllProductsServlet", urlPatterns = "/all-products")
public class AllProductsServlet extends HttpServlet {
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
        try{
            Connection dbcon = dataSource.getConnection();
            String viewQuery = "select p.id, p.name, p.price, p.qty, p.description, p.manufacturer, group_concat(imgSrc) as images " +
                    "from products as p, images as i where p.id = i.product_id group by p.id;";

            PreparedStatement statement = dbcon.prepareStatement(viewQuery);
            ResultSet rs = statement.executeQuery();
            JsonArray jsonArray = new JsonArray();
            while(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String qty = rs.getString("qty");
                String description = rs.getString("description");
                String manufacturer = rs.getString("manufacturer");
                String images = rs.getString("images");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("name", name);
                jsonObject.addProperty("price", price);
                jsonObject.addProperty("qty", qty);
                jsonObject.addProperty("description", description);
                jsonObject.addProperty("manufacturer", manufacturer);
                jsonObject.addProperty("images", images);

                jsonArray.add(jsonObject);
            }
            out.write('[');
            out.write(jsonArray.toString());

            HttpSession session = request.getSession();
            Integer userIdSession = (Integer) session.getAttribute("user_id");
            if (userIdSession != null) {
                out.write(',');
                RequestDispatcher reqDispatcher = request.getRequestDispatcher("/order-history");
                reqDispatcher.include(request, response);
            }
            System.out.println("done123");
            out.write(']');

            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            System.out.println(e.getMessage());
            System.out.println(e);
            response.setStatus(500);
        } finally {
            out.close();
        }

    }


}
