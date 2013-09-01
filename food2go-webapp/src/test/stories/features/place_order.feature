Feature: Place order

In order to order foods for dinner,
as a Customer,
I want to place an order.

Background: Restaurants ready
Given restaurants ready

Scenario: Customer places an pending order

When I submit delivery address and delivery time
Then an pending order is placed
And all available restaurants are listed
When I pick desired menu items from a restaurant
Then order items are updated
When I provide my credit card
Then order is updated with the payment information given   

Scenario: Customer tries to place an pending order but failed for undeliverable address

Given my delivery address is not in service area of any restaurant  
When I submit delivery address and delivery time
Then an pending order is not placed

Scenario: Customer tries to place an pending order but failed for undeliverable time

Given my delivery time is available of any restaurant  
When I submit delivery address and delivery time
Then an pending order is not placed