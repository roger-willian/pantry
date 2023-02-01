@Shopping @ShoppingCart @ShoppingList @Interface @Database
  Feature: Fetch a product from the shopping list to the shopping cart

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

    Scenario: Fetch a whole item from the list
      When I fetch 2 units of the item with id 1 to my shopping cart, costing $ 0.10 per unit and expiring on "01/03/2023"
      Then the last Fetch from List response should be "OK_ALL"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And my shopping cart should have 2 "un" of "bread", costing $ 0.10 per unit and expiring on "01/03/2023"
      And my shopping list should have exactly 2 items, including:
        | id  | qty | unit    | product |
        | 5   | 4   | l       | milk    |
        | 2   | 1   | bottle  | water   |
      But my shopping list should not have an item with id 1

    Scenario: Fetch more than a whole item from the list
      When I fetch 3 units of the item with id 1 to my shopping cart, costing $ 0.10 per unit and expiring on "01/03/2023"
      Then the last Fetch from List response should be "OK_ALL"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And my shopping cart should have 3 "un" of "bread", costing $ 0.10 per unit and expiring on "01/03/2023"
      And my shopping list should have exactly 2 items, including:
        | id  | qty | unit    | product |
        | 5   | 4   | l       | milk    |
        | 2   | 1   | bottle  | water   |
      But my shopping list should not have an item with id 1

    Scenario: Fetch less than a whole item from the list
      When I fetch 1 units of the item with id 1 to my shopping cart, costing $ 0.10 per unit and expiring on "01/03/2023"
      Then the last Fetch from List response should be "OK_SOME"
      And my shopping cart should have exactly 4 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And my shopping cart should have 1 "un" of "bread", costing $ 0.10 per unit and expiring on "01/03/2023"
      And my shopping list should have exactly 3 items, including:
        | id  | qty | unit    | product |
        | 1   | 1   | un      | bread   |
        | 5   | 4   | l       | milk    |
        | 2   | 1   | bottle  | water   |

    Scenario: Fetch an item from the list that was in the shopping cart already
      When I fetch 2 units of the item with id 5 to my shopping cart, costing $ 3.29 per unit and expiring on "08/04/2023"
      Then the last Fetch from List response should be "OK_SOME"
      And my shopping cart should have exactly 3 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      But my shopping cart should have 3.5 "l" of "milk", costing $ 3.29 per unit and expiring on "08/04/2023"
      And my shopping list should have exactly 3 items, including:
        | id  | qty | unit    | product |
        | 1   | 2   | un      | bread   |
        | 2   | 1   | bottle  | water   |
      But my shopping list should have 2 "l" of "milk"

    Scenario Outline: Try to fetch an invalid item from the list

      A valid item has a valid id, a positive quantity, a non-negative price and a valid expiration date.

      When I fetch <qty> units of the item with id <id> to my shopping cart, costing $ <price> per unit and expiring on "<expiration>"
      Then the last Fetch from List response should be "<status>"
      Then my shopping cart should have exactly 3 items, including:
        | id  | qty | unit  | product | price | expiration  |
        | 1   | 10  | kg    | rice    | 4.99  | 06/05/2024  |
        | 2   | 1.5 | l     | milk    | 3.29  | 08/04/2023  |
        | 7   | 3   | cans  | beans   | 5.36  | 15/12/2023  |
      And my shopping list should have exactly 3 items, including:
        | id  | qty | unit    | product |
        | 1   | 2   | un      | bread   |
        | 5   | 4   | l       | milk    |
        | 2   | 1   | bottle  | water   |

      Examples:
        | id  | qty | price | expiration | status     |
        | -1  |  1  |  1.00 | 01/01/2100 | NOT_FOUND  |
        |  1  | -1  |  1.00 | 01/01/2100 | INVALID    |
        |  1  |  0  |  1.00 | 01/01/2100 | INVALID    |
        |  1  |  1  | -1.00 | 01/01/2100 | INVALID    |
        |  1  |  1  |  1.00 | 30/02/2100 | INVALID    |