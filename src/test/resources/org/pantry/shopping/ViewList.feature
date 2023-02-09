@Shopping @ShoppingList
  Feature: View list

    View the shopping list.

    Background: There are some items in the shopping list
      Given I have the following items in my shopping list:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |

    @Interface
    Scenario: Look

      View the products currently in the shopping list.

      When I look at my shopping list
      Then I should see exactly 3 items in my shopping list, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |