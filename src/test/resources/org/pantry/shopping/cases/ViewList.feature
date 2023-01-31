@Shopping @ShoppingList
  Feature: View the shopping list

    Background: There are some items in the shopping list
      Given I have the following items in my shopping list:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |

    @Interface
    Scenario: View the products currently in the shopping list
      When I look at my shopping list
      Then I should see exactly 3 items in my shopping list, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 5   | 1.5 | l     | milk    |
        | 2   | 3   | cans  | beans   |

    @Interface
    Scenario: View a product that has just been added to the shopping list
      When I add 1 "un" of "watermelon" to my shopping list
      And I look at my shopping list
      Then I should see exactly 4 items in my shopping list, including:
        | id  | qty | unit  | product     |
        | 1   | 10  | kg    | rice        |
        | 5   | 1.5 | l     | milk        |
        | 2   | 3   | cans  | beans       |
      And I should see 1 "un" of "watermelon" in my shopping list

    @Interface
    Scenario: Try to view a product that has just been removed from the shopping list
      When I delete the item with id 5 from my shopping list
      And I look at my shopping list
      Then I should see exactly 2 items in my shopping list, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 2   | 3   | cans  | beans   |
      But I should not see any item with id 5 in my shopping list