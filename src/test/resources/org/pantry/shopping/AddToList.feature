@Shopping @ShoppingList @Interface @Database
Feature: Add to list

  Add items to the shopping list.

  Background: There are some items in my shopping list
    Given I have the following items in my shopping list:
      | id  | qty | unit  | product |
      | 1   | 10  | kg    | rice    |
      | 5   | 1.5 | l     | milk    |
      | 2   | 3   | cans  | beans   |

    Scenario: Add new product

      Add a new item to the shopping list.

      When I add 1 "un" of "watermelon" to my shopping list
      Then the last Add to List response should be "OK_NEW"
      And my shopping list should have exactly 4 items, including:
        | id  | qty | unit  | product     |
        | 1   | 10  | kg    | rice        |
        | 5   | 1.5 | l     | milk        |
        | 2   | 3   | cans  | beans       |
      And my shopping list should have 1 "un" of "watermelon"

    Scenario: Increase quantity

      Add a repeated item to the shopping list.

      When I add 0.5 "l" of "milk" to my shopping list
      Then the last Add to List response should be "OK_INCREASED"
      And my shopping list should have exactly 3 items, including:
        | id  | qty | unit  | product |
        | 1   | 10  | kg    | rice    |
        | 2   | 3   | cans  | beans   |
      And my shopping list should have 2 "l" of "milk"

    Scenario Outline: Invalid products

      Try to add invalid products to the shopping list.
      Valid items are those whose quantity is greater than zero, and whose unit and name are non-empty after trimmed.

      When I add <qty> <unit> of <product> to my shopping list
      Then the last Add to List response should be "INVALID"
      And my shopping list should not have any <unit> of <product>

      Examples:
        | qty | unit  | product     |
        | -1  | "kg"  | "potatoes"  |
        |  0  | "kg"  | "potatoes"  |
        |  1  | " "   | "potatoes"  |
        |  1  | "kg"  | " "         |
