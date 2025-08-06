import React, { useState, useEffect } from 'react';
import FoodItemForm from './FoodItemForm';
import FoodItemList from './FoodItemList';
import SearchBar from './SearchBar';
import CSVExport from './CSVExport';
import EditFoodItemModal from './EditFoodItemModal';
import { foodItemsAPI } from '../services/api';

const FoodTracker = () => {
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
      console.log('Loading food items...');
      const response = await foodItemsAPI.getAll();
      console.log('Food items loaded:', response.data);
      setFoodItems(response.data || []);
    } catch (err) {
      console.error('Error loading food items:', err);
      if (err.response?.status === 401) {
        setError('Please log in to view your food items.');
      } else if (err.response?.status === 403) {
        setError('You do not have permission to view food items.');
      } else {
        setError('Failed to load food items. Please make sure you are logged in and the backend server is running.');
      }
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

    // Filter by active tab
    switch (activeTab) {
      case 'recent':
        const sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);
        filtered = filtered.filter(item => 
          new Date(item.createdAt) >= sevenDaysAgo
        );
        break;
      case 'consumed':
        filtered = filtered.filter(item => item.consumedDate);
        break;
      case 'with-calories':
        filtered = filtered.filter(item => item.calorie && item.calorie > 0);
        break;
      default:
        // 'all' - no additional filtering
        break;
    }

    setFilteredItems(filtered);
  };

  const addFoodItem = async (newItem) => {
    try {
      setError('');
      const response = await foodItemsAPI.create(newItem);
      setFoodItems([...foodItems, response.data]);
      setSuccess('Food item added successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Failed to add food item. Please try again.');
      console.error('Error adding food item:', err);
    }
  };

  const updateFoodItem = async (id, updatedItem) => {
    try {
      setError('');
      const response = await foodItemsAPI.update(id, updatedItem);
      setFoodItems(foodItems.map(item => 
        item.id === id ? response.data : item
      ));
      setSuccess('Food item updated successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Failed to update food item. Please try again.');
      console.error('Error updating food item:', err);
    }
  };

  const deleteFoodItem = async (id) => {
    if (window.confirm('Are you sure you want to delete this food item?')) {
      try {
        setError('');
        await foodItemsAPI.delete(id);
        setFoodItems(foodItems.filter(item => item.id !== id));
        setSuccess('Food item deleted successfully!');
        setTimeout(() => setSuccess(''), 3000);
      } catch (err) {
        setError('Failed to delete food item. Please try again.');
        console.error('Error deleting food item:', err);
      }
    }
  };

  const handleEditItem = (item) => {
    setEditingItem(item);
    setIsEditModalOpen(true);
  };

  const handleEditSubmit = async (updatedItem) => {
    await updateFoodItem(editingItem.id, updatedItem);
    setIsEditModalOpen(false);
    setEditingItem(null);
  };

  const handleEditCancel = () => {
    setIsEditModalOpen(false);
    setEditingItem(null);
  };

  const getTotalCalories = () => {
    return filteredItems.reduce((total, item) => {
      return total + (item.calorie || 0);
    }, 0);
  };

  const getItemsCount = () => {
    return {
      total: foodItems.length,
      filtered: filteredItems.length,
      consumed: foodItems.filter(item => item.consumedDate).length,
      withCalories: foodItems.filter(item => item.calorie && item.calorie > 0).length
    };
  };

  const counts = getItemsCount();
  const totalCalories = getTotalCalories();

  return (
    <div className="food-tracker">
      <div className="container">
        <header className="app-header">
          <h1>🍎 Food Keeper</h1>
          <p>Track your daily food consumption and monitor your calorie intake</p>
        </header>

        {/* Statistics Dashboard */}
        <div className="stats-grid">
          <div className="stat-card">
            <h3>{counts.total}</h3>
            <p>Total Items</p>
          </div>
          <div className="stat-card">
            <h3>{counts.consumed}</h3>
            <p>Consumed Items</p>
          </div>
          <div className="stat-card">
            <h3>{totalCalories}</h3>
            <p>Total Calories</p>
          </div>
          <div className="stat-card">
            <h3>{counts.withCalories}</h3>
            <p>Items with Calories</p>
          </div>
        </div>

        {/* Add Food Item Form */}
        <div className="form-section">
          <h2>Add New Food Item</h2>
          <FoodItemForm onSubmit={addFoodItem} />
        </div>

        {/* Search and Filter Section */}
        <div className="search-section">
          <SearchBar 
            searchTerm={searchTerm}
            onSearchChange={setSearchTerm}
          />
          
          {/* Filter Tabs */}
          <div className="filter-tabs">
            <button 
              className={activeTab === 'all' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('all')}
            >
              All ({counts.total})
            </button>
            <button 
              className={activeTab === 'recent' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('recent')}
            >
              Recent
            </button>
            <button 
              className={activeTab === 'consumed' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('consumed')}
            >
              Consumed ({counts.consumed})
            </button>
            <button 
              className={activeTab === 'with-calories' ? 'tab active' : 'tab'}
              onClick={() => setActiveTab('with-calories')}
            >
              With Calories ({counts.withCalories})
            </button>
          </div>
        </div>

        {/* Export Section */}
        <div className="export-section">
          <CSVExport foodItems={filteredItems} />
        </div>

        {/* Messages */}
        {error && <div className="message error">{error}</div>}
        {success && <div className="message success">{success}</div>}

        {/* Food Items List */}
        <div className="list-section">
          <h2>
            Food Items 
            {searchTerm && ` (filtered: ${filteredItems.length})`}
            {activeTab !== 'all' && ` - ${activeTab.charAt(0).toUpperCase() + activeTab.slice(1).replace('-', ' ')}`}
          </h2>
          
          {loading ? (
            <div className="loading">Loading food items...</div>
          ) : (
            <FoodItemList 
              items={filteredItems}
              onDelete={deleteFoodItem}
              onEdit={handleEditItem}
              emptyMessage="No food items found. Add some food items to see them here!"
            />
          )}
        </div>

        {/* Edit Modal */}
        {isEditModalOpen && (
          <EditFoodItemModal
            isOpen={isEditModalOpen}
            foodItem={editingItem}
            onSave={handleEditSubmit}
            onClose={handleEditCancel}
          />
        )}
      </div>
    </div>
  );
};

export default FoodTracker; 