import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/SmartDoorbellList")

public class SmartDoorbellList extends HttpServlet {

    /* SmartDoorbell Page Displays all the smart doorbells and their Information in Smart Homes */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
		HashMap<String, SmartDoorbell> smartDoorbells = new HashMap<String, SmartDoorbell>();
		try {
            smartDoorbells = MySqlDataStoreUtilities.getDoorbells();
		} catch(Exception e) {
		}

        String name = null;
        String CategoryName = request.getParameter("maker");

        /* Checks the Tablets type whether it is eufy or google or blink */
        HashMap<String, SmartDoorbell> hm = new HashMap<String, SmartDoorbell>();
        if (CategoryName == null) {
            hm.putAll(smartDoorbells);
            name = "";
        } else {
            if (CategoryName.equals("Eufy")) {
                for (Map.Entry<String, SmartDoorbell> entry : smartDoorbells.entrySet()) {
                    if (entry.getValue().getRetailer().equals("Eufy")) {
                        hm.put(entry.getValue().getId(), entry.getValue());
                    }
                }
                name = "Eufy";
            } else if (CategoryName.equals("Google")) {
                for (Map.Entry<String, SmartDoorbell> entry : smartDoorbells.entrySet()) {
                    if (entry.getValue().getRetailer().equals("Google")) {
                        hm.put(entry.getValue().getId(), entry.getValue());
                    }
                }
                name = "Google";
            } else if (CategoryName.equals("Ring")) {
                for (Map.Entry<String, SmartDoorbell> entry : smartDoorbells.entrySet()) {
                    if (entry.getValue().getRetailer().equals("Ring")) {
                        hm.put(entry.getValue().getId(), entry.getValue());
                    }
                }
                name = "Ring";
            }
        }

		/* Header, Left Navigation Bar are Printed.

		All the doorbells information are displayed in the Content Section

		and then Footer is Printed*/

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>" + name + "SmartDoorbell's</a>");
        pw.print("</h2><div class='entry'><table id='bestseller'>");
        int i = 1;
        int size = hm.size();
        for (Map.Entry<String, SmartDoorbell> entry : hm.entrySet()) {
            SmartDoorbell doorbell = entry.getValue();
            if (i % 3 == 1) pw.print("<tr>");
            pw.print("<td><div id='shop_item'>");
            pw.print("<h3>" + doorbell.getName() + "</h3>");
            pw.print("<strong>$" + doorbell.getPrice() + "</strong><ul>");
            pw.print("<li id='item'><img src='images/SmartDoorbells/" + doorbell.getImage() + "' alt='' /></li>");
            pw.print("<h5>" + doorbell.getDescription() + "</h5></li>");

            pw.print("<li><form method='post' action='Cart'>" +
                    "<input type='hidden' name='name' value='"+entry.getKey()+"'>"+
                    "<input type='hidden' name='type' value='doorbells'>"+
                    "<input type='hidden' name='maker' value='"+CategoryName+"'>"+
                    "<input type='hidden' name='access' value=''>"+
                    "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
			pw.print("<li><form method='post' action='WriteReview'>"+"<input type='hidden' name='name' value='"+entry.getKey()+"'>"+
					"<input type='hidden' name='type' value='doorbells'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='price' value='"+doorbell.getPrice()+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' value='WriteReview' class='btnreview'></form></li>");
			pw.print("<li><form method='post' action='ViewReview'>"+"<input type='hidden' name='name' value='"+entry.getKey()+"'>"+
					"<input type='hidden' name='type' value='doorbells'>"+
					"<input type='hidden' name='maker' value='"+CategoryName+"'>"+
					"<input type='hidden' name='access' value=''>"+
					"<input type='submit' value='ViewReview' class='btnreview'></form></li>");
            pw.print("</ul></div></td>");
            if (i % 3 == 0 || i == size)
                pw.print("</tr>");
            i++;
        }
        pw.print("</table></div></div></div>");
        utility.printHtml("Footer.html");
    }
}
