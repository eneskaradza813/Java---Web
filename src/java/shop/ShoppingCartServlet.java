
package shop;

import shop.model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static shop.WebShopServlet.PRODUCTS_KEY;
import shop.model.ShoppingCart;
import shop.model.ShoppingCartItem;


@WebServlet(name = "ShoppingCartServlet", urlPatterns = {"/ShoppingCartServlet"})
public class ShoppingCartServlet extends HttpServlet {


    public static final String SHOPPING_CART_KEY = "korpa";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Proizvodi u korpi</title>");            
            out.println("</head>");
            out.println("<body>");
            HttpSession session = request.getSession();
            ShoppingCart shoppingCart =(ShoppingCart) session.getAttribute(SHOPPING_CART_KEY);
            if(shoppingCart == null){
                shoppingCart = new ShoppingCart();
                session.setAttribute(SHOPPING_CART_KEY, shoppingCart);
            }
            int quantity = Integer.parseInt(request.getParameter("productQuantity"));
            int productID = Integer.parseInt(request.getParameter("productID"));
            List<Product> productList = (List<Product>) getServletContext().getAttribute(PRODUCTS_KEY);
            for(Product product : productList){
                if(product.getId() == productID){
                    shoppingCart.addShoppingCartItem(product, quantity);
                    break;
                }
            }
           List<ShoppingCartItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
            if(!shoppingCartItems.isEmpty()){
                out.println("<h3>Dodani artikli u korpi</h3>");
                out.println("<table border = '1'>");
                out.println("<tr bgcolor = 'lightgray'>"
                        + "<th>Naziv</th>"
                        + "<th>Jedinicna cijena</th>"
                        + "<th>Kolicina</th>"
                        + "<th>Ukupna Cijena</th>"
                        + "</tr>");
                for(ShoppingCartItem item : shoppingCartItems){
                    out.println("<tr>"
                        + "<td>"+ item.getProduct().getProductName()+ "</td>"
                        + "<td>"+item.getProduct().getPrice()+"</td>"
                        + "<td>"+ item.getQuantity()+"</td>"
                        + "<td>"+item.getTotalPrice()+"</td>"
                        + "</tr>");
                }
                out.println("</table>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


}
