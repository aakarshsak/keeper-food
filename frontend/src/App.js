import React, { useState, useEffect } from 'react';
import './App.css';
import FoodItemForm from './components/FoodItemForm';
import FoodItemList from './components/FoodItemList';
import SearchBar from './components/SearchBar';
import CSVExport from './components/CSVExport';
import EditFoodItemModal from './components/EditFoodItemModal';
import { foodItemsAPI } from './services/api';

function App() {
  const [foodItems, setFoodItems] = useState([]);
  const [filteredItems, setFilteredItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [activeTab, setActiveTab] = useState('all');
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  // Load food items on component mount
  useEffect(() => {
    loadFoodItems();
  }, []);

  // Filter items based on search term and active tab
  useEffect(() => {
    filterItems();
  }, [foodItems, searchTerm, activeTab]);

  const loadFoodItems = async () => {
    try {
      setLoading(true);
      setError('');
      const response = await foodItemsAPI.getAll();
      setFoodItems(response.data);
    } catch (err) {
      setError('Failed to load food items. Please make sure the backend server is running.');
      console.error('Error loading food items:', err);
    } finally {
      setLoading(false);
    }
  };

  const filterItems = () => {
    let filtered = foodItems;

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(item =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        (item.description && item.description.toLowerCase().includes(searchTerm.toLowerCase()))
      );
    }

    // Filter by tab
    if (activeTab === 'recent') {
      const weekAgo = new Date();
      weekAgo.setDate(weekAgo.getDate() - 7);
      filtered = filtered.filter(item => new Date(item.createdAt) >= weekAgo);
    } else if (activeTab === 'consumed') {
      filtered = filtered.filter(item => item.consumedDate);
    } else if (activeTab === 'with-calories') {
      filtered = filtered.filter(item => item.calorie && item.calorie > 0);
    }

    setFilteredItems(filtered);
  };

  const handleAddFoodItem = async (foodItemData) => {
    try {
      setError('');
      setSuccess('');
      await foodItemsAPI.create(foodItemData);
      setSuccess('Food item added successfully!');
      loadFoodItems(); // Reload the list
      
      // Clear success message after 3 seconds
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Failed to add food item. Please try again.');
      console.error('Error adding food item:', err);
    }
  };

  const handleDeleteFoodItem = async (id) => {
    if (window.confirm('Are you sure you want to delete this food item?')) {
      try {
        setError('');
        await foodItemsAPI.delete(id);
        setSuccess('Food item deleted successfully!');
        loadFoodItems(); // Reload the list
        
        // Clear success message after 3 seconds
        setTimeout(() => setSuccess(''), 3000);
      } catch (err) {
        setError('Failed to delete food item. Please try again.');
        console.error('Error deleting food item:', err);
      }
    }
  };

  const handleEditFoodItem = (item) => {
    setEditingItem(item);
    setIsEditModalOpen(true);
  };

  const handleEditModalClose = () => {
    setIsEditModalOpen(false);
    setEditingItem(null);
  };

  const handleEditModalSave = () => {
    setSuccess('Food item updated successfully!');
    loadFoodItems(); // Reload the list
    setTimeout(() => setSuccess(''), 3000);
  };

  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  const clearMessages = () => {
    setError('');
    setSuccess('');
  };

  const tabs = [
    { id: 'all', label: 'All Items', count: foodItems.length },
    { id: 'recent', label: 'Recent', count: foodItems.filter(item => {
      const weekAgo = new Date();
      weekAgo.setDate(weekAgo.getDate() - 7);
      return new Date(item.createdAt) >= weekAgo;
    }).length },
    { id: 'consumed', label: 'Consumed', count: foodItems.filter(item => item.consumedDate).length },
    { id: 'with-calories', label: 'With Calories', count: foodItems.filter(item => item.calorie && item.calorie > 0).length }
  ];

  return (
    <div className="App">
      <div className="container">
        <header className="app-header">
          <h1>🍽️ Food Tracker</h1>
          <p>Track your food consumption and monitor your calorie intake!</p>
        </header>

        {/* Alert Messages */}
        {error && (
          <div className="alert alert-error">
            {error}
            <button onClick={clearMessages} style={{float: 'right', background: 'none', border: 'none', cursor: 'pointer'}}>×</button>
          </div>
        )}
        {success && (
          <div className="alert alert-success">
            {success}
            <button onClick={clearMessages} style={{float: 'right', background: 'none', border: 'none', cursor: 'pointer'}}>×</button>
          </div>
        )}

        {/* Add Food Item Form */}
        <div className="card">
          <h2>Log Food Consumption</h2>
          <FoodItemForm onSubmit={handleAddFoodItem} />
        </div>

        {/* CSV Export */}
        <div className="card">
          <CSVExport />
        </div>

        {/* Search Bar */}
        <SearchBar onSearch={handleSearch} />

        {/* Tabs */}
        <div className="tabs">
          {tabs.map(tab => (
            <button
              key={tab.id}
              className={`tab ${activeTab === tab.id ? 'active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              {tab.label} ({tab.count})
            </button>
          ))}
        </div>

        {/* Food Items List */}
        <div className="card">
          <h2>Your Food Log</h2>
          {loading ? (
            <div className="loading">Loading food items...</div>
          ) : (
            <FoodItemList 
              items={filteredItems} 
              onDelete={handleDeleteFoodItem}
              onEdit={handleEditFoodItem}
              emptyMessage={
                searchTerm 
                  ? `No food items found matching "${searchTerm}"` 
                  : activeTab === 'recent' 
                    ? 'No food items logged in the last 7 days'
                    : activeTab === 'consumed'
                      ? 'No food items with consumption dates logged yet'
                      : activeTab === 'with-calories'
                        ? 'No food items with calorie information logged yet'
                        : 'No food items logged yet. Start tracking your consumption above!'
              }
            />
          )}
        </div>
        
        {/* Edit Modal */}
        <EditFoodItemModal
          isOpen={isEditModalOpen}
          onClose={handleEditModalClose}
          foodItem={editingItem}
          onSave={handleEditModalSave}
        />
      </div>
    </div>
  );
}

export default App;