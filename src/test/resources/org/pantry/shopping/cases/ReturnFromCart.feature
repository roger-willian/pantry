@Shopping @ShoppingCart @ShoppingList @Interface @Database
  Feature: Move items from the shopping cart to the shopping list

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

    Scenario Outline: Move a whole item to the shopping list
      When I return <qty> units of the item with id <id> from the shopping cart to the shopping list
      Then the last Return from Cart response should be "OK_ALL"
      And my shopping list should have <lst_qty> "<unit>" of "<product>"
      But my shopping cart should not have an item with id <id>
      Examples:
        | id | qty  | unit | product | lst_qty |
        | 1  | 10.0 | kg   | rice    |    10.0 |
        | 2  |  1.5 | l    | milk    |     5.5 |

    Scenario Outline: Move less than a whole item to the shopping list
      When I return <returned> units of the item with id <id> from the shopping cart to the shopping list
      Then the last Return from Cart response should be "OK_SOME"
      And my shopping list should have <to_fetch> "<unit>" of "<product>"
      And my shopping cart should have <fetched> "<unit>" of "<product>", costing $ <price> per unit and expiring on "<expiration>"
      Examples:
        | id | unit | product | price | expiration | returned | to_fetch | fetched |
        |  1 | kg   | rice    |  4.99 | 06/05/2024 |      7.0 |      7.0 |     3.0 |
        |  2 | l    | milk    |  3.29 | 08/04/2023 |      1.0 |      5.0 |     0.5 |

    Scenario Outline: Try to return an invalid item from the cart

      A valid item has a valid id, and a quantity that is both positive and not greater than there currently is in the cart.

      When I return <qty> units of the item with id <id> from the shopping cart to the shopping list
      Then the last Return from Cart response should be "<response>"
      And my shopping cart should have exactly 3 items, including:
        | id | qty  | unit  | product | price | expiration  |
        |  1 | 10.0 | kg    | rice    |  4.99 | 06/05/2024  |
        |  2 |  1.5 | l     | milk    |  3.29 | 08/04/2023  |
        |  7 |  3.0 | cans  | beans   |  5.36 | 15/12/2023  |
      And my shopping list should have exactly 3 items, including:
        | id | qty | unit    | product |
        |  1 | 2.0 | un      | bread   |
        |  5 | 4.0 | l       | milk    |
        |  2 | 1.0 | bottle  | water   |
      Examples:
        | id | qty  | response  |
        | 10 |  1.0 | NOT_FOUND |
        |  7 |  0.0 | INVALID   |
        |  7 | -1.0 | INVALID   |
        |  7 |  4.0 | TOO_MANY  |