@Shopping @ShoppingCart @Interface @Database
  Feature: Remove items from the cart without moving them to the shopping list

    Background: There are some items in my shopping cart
      Given I have the following items in my shopping cart:
        | id | qty  | unit  | product | price | expiration  |
        |  1 | 10.0 | kg    | rice    | 4.99  | 06/05/2024  |
        |  5 |  1.5 | l     | milk    | 3.29  | 08/04/2023  |
        |  2 |  3.0 | cans  | beans   | 5.36  | 15/12/2023  |

    Scenario: Remove a whole item from the shopping cart
      When I remove 10 units of the item with id 1 from the shopping cart
      Then the last Delete fom Cart response should be "OK_ALL"
      And my shopping cart should have exactly 2 items, including:
        | id | qty  | unit  | product | price | expiration  |
        |  5 |  1.5 | l     | milk    | 3.29  | 08/04/2023  |
        |  2 |  3.0 | cans  | beans   | 5.36  | 15/12/2023  |
      But my shopping cart should not have an item with id 1

    Scenario: Remove less than a whole item from the shopping cart
      When I remove 7 units of the item with id 1 from the shopping cart
      Then the last Delete fom Cart response should be "OK_SOME"
      And my shopping cart should have exactly 3 items, including:
        | id | qty  | unit  | product | price | expiration  |
        |  5 |  1.5 | l     | milk    | 3.29  | 08/04/2023  |
        |  2 |  3.0 | cans  | beans   | 5.36  | 15/12/2023  |
      But my shopping cart should have 3 "kg" of "rice", costing $ 4.99 per unit and expiring on "06/05/2024"

    Scenario Outline: Try to remove an invalid item from the shopping cart

      A valid item has a valid id, and a quantity that is both positive and not greater than the quantity in the cart.

      When I remove <qty> units of the item with id <id> from the shopping cart
      Then the last Delete fom Cart response should be "<response>"
      And my shopping cart should have exactly 3 items, including:
        | id | qty  | unit  | product | price | expiration  |
        |  1 | 10.0 | kg    | rice    | 4.99  | 06/05/2024  |
        |  5 |  1.5 | l     | milk    | 3.29  | 08/04/2023  |
        |  2 |  3.0 | cans  | beans   | 5.36  | 15/12/2023  |
      Examples:
        | id | qty  | response  |
        | 10 |  1.0 | NOT_FOUND |
        |  1 | -1.0 | INVALID   |
        |  1 | 11.0 | TOO_MANY  |