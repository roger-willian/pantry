@Shopping @ShoppingCart
  Feature: View cart

    View the items in my shopping cart.

    Background: There are some items in my shopping cart
      Given I have the following items in my shopping cart:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |

    @Interface
    Scenario: Look

      View the products currently in my shopping cart.

      When I look at my shopping cart
      Then I should see exactly 3 items in my shopping cart, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |