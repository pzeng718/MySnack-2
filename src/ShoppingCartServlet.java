import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/shopping-cart")
public class ShoppingCartServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mysnack");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        PrintWriter out = response.getWriter();
        HashMap<String, ArrayList<String>> items = (HashMap<String, ArrayList<String>>) session.getAttribute("items");
        JsonObject responseJsonObject = new JsonObject();
        if(items == null){
            responseJsonObject.addProperty("itemExists", false);
            session.setAttribute("totalPrice", 0);
        }else{
            responseJsonObject.addProperty("itemExists", true);
            Gson gson = new Gson();
            JsonObject json = gson.toJsonTree(items).getAsJsonObject();
            responseJsonObject.add("items", json);

            double totalPrice = 0;
            for(String productId : items.keySet()){
                ArrayList<String> itemInfo = items.get(productId);
                totalPrice += Double.parseDouble(itemInfo.get(1)) * Integer.parseInt(itemInfo.get(2));
            }

            session.setAttribute("totalPrice", totalPrice);
        }

        out.write(responseJsonObject.toString());
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String productName = request.getParameter("name");
        String productId = request.getParameter("productId");
        String price = request.getParameter("price");
        String qty = request.getParameter("qtySelected");

        PrintWriter out = response.getWriter();

        HashMap<String, ArrayList<String>> items = (HashMap<String, ArrayList<String>>) session.getAttribute("items");
        if(items == null){
            items = new HashMap<>();
            ArrayList<String> itemInfo = new ArrayList<>();

            itemInfo.add(productName);
            itemInfo.add(price);
            itemInfo.add(qty);

            items.put(productId, itemInfo);
            session.setAttribute("items", items);
        }else{
            if(items.containsKey(productId)){
                ArrayList<String> itemInfo = items.get(productId);
                itemInfo.set(2, String.valueOf(Integer.parseInt(itemInfo.get(2)) + Integer.parseInt(qty)));
            }else{
                ArrayList<String> itemInfo = new ArrayList<>();
                itemInfo.add(productName);
                itemInfo.add(price);
                itemInfo.add(qty);

                items.put(productId, itemInfo);
            }
        }
        JsonObject responseJsonObject = new JsonObject();
        Gson gson = new Gson();
        JsonObject json = gson.toJsonTree(items).getAsJsonObject();
        responseJsonObject.add("items", json);

        out.write(responseJsonObject.toString());

        out.close();
        response.setStatus(200);
    }
}
