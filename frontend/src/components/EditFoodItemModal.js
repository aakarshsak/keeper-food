import React, { useState, useEffect } from 'react';
import { foodItemsAPI } from '../services/api';

const EditFoodItemModal = ({ isOpen, onClose, foodItem, onSave }) => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    calorie: '',
    quantity: '',
    consumedDate: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  // Initialize form data when food item changes
  useEffect(() => {
    if (foodItem) {
      setFormData({
        name: foodItem.name || '',
        description: foodItem.description || '',
        calorie: foodItem.calorie || '',
        quantity: foodItem.quantity || '',
        consumedDate: foodItem.consumedDate ? 
          new Date(foodItem.consumedDate).toISOString().slice(0, 16) : ''
      });
    }
  }, [foodItem]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.name.trim()) {
      setError('Food name is required');
      return;
    }

    try {
      setIsLoading(true);
      setError('');
      
      const updateData = {
        name: formData.name.trim(),
        description: formData.description.trim(),
        calorie: formData.calorie ? parseInt(formData.calorie) : null,
        quantity: formData.quantity.trim(),
        consumedDate: formData.consumedDate || null
      };

      await foodItemsAPI.update(foodItem.id, updateData);
      onSave(); // Callback to refresh the list
      onClose(); // Close the modal
      
    } catch (err) {
      setError('Failed to update food item. Please try again.');
      console.error('Error updating food item:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleClose = () => {
    setError('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={handleClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>✏️ Edit Food Item</h3>
          <button 
            className="modal-close" 
            onClick={handleClose}
            disabled={isLoading}
          >
            ×
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="modal-body">
          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}
          
          <div className="form-group">
            <label htmlFor="edit-name">Food Name *</label>
            <input
              type="text"
              id="edit-name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="form-control"
              placeholder="Enter food name"
              maxLength="100"
              required
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="edit-description">Description</label>
            <textarea
              id="edit-description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              className="form-control"
              placeholder="Optional description"
              maxLength="500"
              rows="3"
              disabled={isLoading}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="edit-calorie">Calories</label>
              <input
                type="number"
                id="edit-calorie"
                name="calorie"
                value={formData.calorie}
                onChange={handleChange}
                className="form-control"
                placeholder="e.g., 250"
                min="0"
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="edit-quantity">Quantity</label>
              <input
                type="text"
                id="edit-quantity"
                name="quantity"
                value={formData.quantity}
                onChange={handleChange}
                className="form-control"
                placeholder="e.g., 1 cup, 200g"
                maxLength="50"
                disabled={isLoading}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="edit-consumedDate">Consumed Date & Time</label>
            <input
              type="datetime-local"
              id="edit-consumedDate"
              name="consumedDate"
              value={formData.consumedDate}
              onChange={handleChange}
              className="form-control"
              disabled={isLoading}
            />
          </div>
          
          <div className="modal-footer">
            <button
              type="button"
              onClick={handleClose}
              className="btn btn-secondary"
              disabled={isLoading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditFoodItemModal; 