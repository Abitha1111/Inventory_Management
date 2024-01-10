package Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

	public static void insertDatabase(String name, int quantity) {
	    try (Connection connection = repository.getConnection()) {
	        String insertQuery = "INSERT INTO products (name, quantity) VALUES (?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

	            preparedStatement.setString(1, name);
	            preparedStatement.setInt(2, quantity);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int autoGeneratedID = generatedKeys.getInt(1);
	                    System.out.println("\nItem added successfully...\n ");
	                }
	            } else {
	                System.out.println("Failed to add the item...");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public static void updatedatabase(String newName, int updateQuantity, int ItemNo) {
		try (Connection connection = repository.getConnection()) {
			String updateQuery = "UPDATE products SET name = ?, quantity = ? WHERE No = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, newName);
				preparedStatement.setInt(2, updateQuantity);
				preparedStatement.setInt(3, ItemNo);
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Item Updated Successfully...\n");

				} else {
					System.out.println("Item not found!\n");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removedatabase(int removeItemNo) {
		try (Connection connection = repository.getConnection()) {
			String deleteQuery = "DELETE FROM products WHERE No = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
				preparedStatement.setInt(1, removeItemNo);
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Item removed successfully...\n");
					try (Connection con = repository.getConnection()) {
						String renumberQuery = "SET @counter=0";
						try (PreparedStatement renumberStatement = con.prepareStatement(renumberQuery)) {
							renumberStatement.executeUpdate();
						}

						String updateQuery = "UPDATE products SET No=@counter:=@counter+1";
						try (PreparedStatement updateStatement = con.prepareStatement(updateQuery)) {
							updateStatement.executeUpdate();
						}
					}
				} else {
					System.out.println("Item not found!\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void searchByName(String name) {

		try (Connection connection = repository.getConnection()) {
			String searchQuery = "SELECT * FROM products WHERE name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int no = resultSet.getInt("No");
				String itemname = resultSet.getString("name");
				int quantity = resultSet.getInt("quantity");
				System.out.println("Number: " + no + ", Name: " + itemname + ", Quantity: " + quantity + "\n");

			}
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void searchByNumber(int number) {

		try (Connection connection = repository.getConnection()) {
			String searchQuery = "SELECT * FROM products WHERE No=?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
				preparedStatement.setInt(1, number);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						int no = resultSet.getInt("No");
						String itemname = resultSet.getString("name");
						int quantity = resultSet.getInt("quantity");
						System.out.println("Number: " + no + ", Name: " + itemname + ", Quantity: " + quantity + "\n");

					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void displayAllItems() {
		try (Connection connection = repository.getConnection()) {
			String searchQuery = "SELECT * FROM products";
			try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					System.out.println("----------------------------------------------------");
					System.out.printf("| %-6s | %-20s | %-9s |\n", "Number", "Name", "Quantity");
					System.out.println("----------------------------------------------------");

					while (resultSet.next()) {
						int no = resultSet.getInt("No");
						String itemName = resultSet.getString("name");
						int quantity = resultSet.getInt("quantity");

						System.out.printf("| %-6d | %-20s | %-9d |\n", no, itemName, quantity);
					}
					System.out.println("----------------------------------------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
