
package shop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shop.model.Product;


public class WebShopServlet extends HttpServlet {

    public static final String PRODUCTS_KEY = "products";
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        List<Product> productsList = new ArrayList<>();
        ServletContext servletContext = getServletContext();
        String filePath = servletContext.getRealPath("products.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine())!=null){
                StringTokenizer tokenizer = new StringTokenizer(line, ";");
                int id = Integer.parseInt(tokenizer.nextToken());
                String productName = tokenizer.nextToken();
                double price = Double.parseDouble(tokenizer.nextToken());
                Product product = new Product(id, productName, price);
                productsList.add(product);
            }
        }catch(IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
        servletContext.setAttribute(PRODUCTS_KEY, productsList);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Products</title>");            
            out.println("</head>");
            out.println("<body>");
            List<Product> productList = (List<Product>) getServletContext().getAttribute(PRODUCTS_KEY);
            if(productList != null && !productList.isEmpty()){
                out.println("<h3>Dostupni proizvodi</h3>");
                out.println("<table border = '1'>");
                out.println("<tr bgcolor = 'lightgray'><th>Naziv</th><th>Cijena</th><th>U korpu</th></tr>");
                for(Product product: productList){
                    out.println("<tr>");
                    out.println("<td>" + product.getProductName()+"</td>");
                    out.println("<td>" + product.getPrice()+"</td>");
                    out.println("<td>");
                    out.println("<form action = 'ShoppingCartServlet' method = 'GET'>");
                    out.println("<input type='number' size = '5' name = 'productQuantity'/>");
                    out.println("<input type = 'hidden' value = '" + product.getId() +"' name = 'productID'/>");
                    out.println("<input type = 'submit' value = 'Dodaj' />");
                    out.println("</form>");
                    out.println("</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }else{
                out.println("<h3>Trenutno nema dostupnih proizvoda</h3>");
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
