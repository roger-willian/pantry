@Shopping @ShoppingList
  Feature: Delete items from the shopping list

    Scenario: Delete an item from the shopping list
      Given I have an item with id 10 in my shopping list
      When I delete the item with id 10 from my shopping list
      Then I should not have any item with id 10 in my shopping list