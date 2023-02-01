@Shopping @ShoppingCart
  Feature: View the items in my shopping cart

    Background: There are some items in my shopping cart
      Given I have the following items in my shopping cart:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |

    @Interface
    Scenario: View the products currently in my shopping cart
      When I look at my shopping cart
      Then I should see exactly 3 items in my shopping cart, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |

    @Interface
    Scenario: View a product that has just been fetched directly to the shopping cart
      When I fetch 1 "un" of "watermelon" to my shopping cart, costing $ 22.00 per unit and expiring on "23/12/2023"
      And I look at my shopping cart
      Then I should see exactly 4 items in my shopping cart, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And I should see 1 "un" of "watermelon" in my shopping cart, costing $ 22.00 per unit and expiring on "23/12/2023"

    @Interface
    Scenario: View a product that has just been fetched from the shopping list to the shopping cart
      Given I have the following items in my shopping list:
        | id  | qty | unit  | product     |
        | 1   | 10  | kg    | rice        |
        | 5   | 1   | un    | watermelon  |
        | 2   | 3   | cans  | beans       |
      When I fetch 2 units of the item with id 5 to my shopping cart, costing $ 22.00 per unit and expiring on "23/12/2023"
      And I look at my shopping cart
      Then I should see exactly 4 items in my shopping cart, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And I should see 2 "un" of "watermelon" in my shopping cart, costing $ 22.00 per unit and expiring on "23/12/2023"

    @Interface
    Scenario: Stop viewing a product that has just been returned from the shopping cart to the shopping list
      Given I return 10 units of the item with id 1 from the shopping cart to the shopping list
      And I look at my shopping cart
      Then I should see exactly 2 items in my shopping cart, including:
        | id  | qty | unit  | product | price | expiration  |
        | 5   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 2   | 3   | cans  | beans   | 5.36  | 15/12/2023  |