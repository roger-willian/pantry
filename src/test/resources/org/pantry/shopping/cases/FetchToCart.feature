@Shopping @ShoppingCart @Interface @Database
  Feature: Fetch items directly without the aid of the shopping list

    Background: There are some items in the shopping list and others in the shopping cart
      Given I have the following items in my shopping list:
        | id  | qty | unit    | product |
        | 1   | 2   | un      | bread   |
        | 5   | 4   | l       | milk    |
        | 2   | 1   | bottle  | water   |
      Given I have the following items in my shopping cart:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |

    Scenario: Fetch an item that is neither in the shopping list nor in the cart
      When I fetch 1 "un" of "watermelon" to my shopping cart, costing $ 22.00 per unit and expiring on "20/08/2023"
      Then the last Fetch to Cart response should be "OK_NEW"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And my shopping cart should have 1 "un" of "watermelon", costing $ 22.00 per unit and expiring on "20/08/2023"

    Scenario: Fetch an item that is not in the shopping list but in the cart
      When I fetch 1 "cans" of "beans" to my shopping cart, costing $ 5.36 per unit and expiring on "15/12/2023"
      Then the last Fetch to Cart response should be "OK_INCREASED"
      And my shopping cart should have exactly 3 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
      But my shopping cart should have 4 "cans" of "beans", costing $ 5.36 per unit and expiring on "15/12/2023"

    @ShoppingList
    Scenario Outline: Fetch a whole or more item that is not in the cart but in the shopping list
      When I fetch <qty> "<unit>" of "<product>" to my shopping cart, costing $ <price> per unit and expiring on "<expiration>"
      Then the last Fetch to Cart response should be "<response>"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      But my shopping cart should have <qty> "<unit>" of "<product>", costing $ <price> per unit and expiring on "<expiration>"
      And my shopping list should not have any "<unit>" of "<product>"
      Examples:
        | qty | unit    | product | price | expiration | response |
        |  1  | bottle  | water   | 1.00  | 25/12/2023 | OK_ALL   |
        |  2  | bottle  | water   | 1.00  | 25/12/2023 | OK_ALL   |

    @ShoppingList
    Scenario: Fetch less than a whole item that is not in the cart but in the shopping list
      When I fetch 0.5 "un" of "bread" to my shopping cart, costing $ 0.5 per unit and expiring on "24/02/2023"
      Then the last Fetch to Cart response should be "OK_SOME"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      But my shopping cart should have 0.5 "un" of "bread", costing $ 0.5 per unit and expiring on "24/02/2023"
      And my shopping list should have 1.5 "un" of "bread"

    Scenario Outline: Try to fetch an invalid item directly to the shopping cart

      A valid item has a positive quantity, non-empty unit and name after trimmed, a non-negative price and a valid expiration date

      When I fetch <qty> <unit> of <product> to my shopping cart, costing $ <price> per unit and expiring on "<expiration>"
      Then the last Fetch to Cart response should be "INVALID"
      And my shopping cart should have exactly 3 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      Examples:
        | qty | unit  | product | price | expiration |
        |  -1 | "un"  | "prod"  |  1.00 | 01/01/2100 |
        |   1 | "  "  | "prod"  |  1.00 | 01/01/2100 |
        |   1 | "un"  | "    "  |  1.00 | 01/01/2100 |
        |   1 | "un"  | "prod"  | -1.00 | 01/01/2100 |
        |   1 | "un"  | "prod"  |  1.00 | 32/05/2100 |
