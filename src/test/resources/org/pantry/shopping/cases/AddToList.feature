@Shopping @ShoppingList
Feature: Add items to the shopping list

  Scenario: add a new item to the shopping list
    Given I have 1 "kg" of "rice" in my shopping list
    When I add 3 "cans" of "beans" to my shopping list
    Then my shopping list should have 1 "kg" of "rice"
    And my shopping list should have 3 "cans" of "beans"

  Scenario: add a repeated item to the shopping list
    Given I have 0.5 "kg" of "potatoes" in my shopping list
    When I add 0.5 "kg" of "potatoes" to my shopping list
    Then my shopping list should have 1 "kg" of "potatoes"

  Scenario: try to add an item with negative quantity to the shopping list
    Given I have 1 "kg" of "rice" in my shopping list
    When I add -1 "cans" of "beans" to my shopping list
    Then my shopping list should not have any "cans" of "beans"

  Scenario: try to add an item with quantity zero to the shopping list
    Given I have 1 "kg" of "rice" in my shopping list
    When I add 0 "cans" of "beans" to my shopping list
    Then my shopping list should not have any "cans" of "beans"

  Scenario: try to add an item with empty unit to the shopping list
    Given I have 1 "kg" of "rice" in my shopping list
    When I add 1 " " of "beans" to my shopping list
    Then my shopping list should not have any " " of "beans"

  Scenario: try to add an item with empty name to the shopping list
    Given I have 1 "kg" of "rice" in my shopping list
    When I add 1 "kg" of " " to my shopping list
    Then my shopping list should not have any "kg" of " "