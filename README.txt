Application Title: JavaFXApp
Application Purpose: This program is a GUI-based inventorying desktop application, allowing its users to manage products and their associated parts.

Author: James Wilson
Email: jwi3581@wgu.edu
Version: 1.0
Date: 12/29/2022

IDE Version: IntelliJ IDEA 2021.1.3 (Community Edition)
JDK Version: Java SE 17.0.5
JavaFX Version: JavaFX-SDK-17.0.1

Directions:
• Launch the program to bring up the main menu.
• From the main menu you can add new parts and products. You can also select a part or product from the tables to modify or delete it.
• To close the program, click the X button at the top right of the window, or navigate to the main menu and click the button labeled "Exit".

Future Enhancement Opportunities: 
• Price columns in both tables should be set so that prices appear with two decimal places automatically.
• The save part/product methods may be an area for improvement. Consecutive try/catch blocks appeared necessary in order to register multiple exception messages at once, but these cluttered up the code.

Error addressed:
The associatedParts ObservableList<Part> was originally assigned as product.getAllAssociatedParts(). This gave them identical references, meaning product.associatedParts might be changed even if the user canceled rather than saving modifications. Instead, associatedParts is now created as a copy of product.associatedParts.