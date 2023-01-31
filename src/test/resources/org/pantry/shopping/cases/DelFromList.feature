@Shopping @ShoppingList
  Feature: Delete items from the shopping list

    Background: There are some items in the shopping list
      Given I have the following items in my shopping list:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |

    @Interface @Database
    Scenario: Delete an item from the shopping list
      When I delete the item with id 2 from my shopping list
      Then the last Delete fom List response should be "OK"
      And my shopping list should have exactly 2 items, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
      But my shopping list should not have an item with id 2

    @Interface @Database
    Scenario: Try to delete an item with wrong id from the shopping list
      When I delete the item with id 10 from my shopping list
      Then  the last Delete fom List response should be "NOT_FOUND"
      And my shopping list should have exactly 3 items, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |