import React from 'react';
import { format, isToday, isYesterday, formatDistanceToNow } from 'date-fns';

const FoodItemList = ({ items, onDelete, emptyMessage }) => {
  if (!items || items.length === 0) {
    return (
      <div className="empty-state">
        <p>{emptyMessage}</p>
      </div>
    );
  }

  const formatDate = (dateString) => {
    if (!dateString) return 'No date set';
    try {
      const date = new Date(dateString);
      if (isToday(date)) return `Today at ${format(date, 'HH:mm')}`;
      if (isYesterday(date)) return `Yesterday at ${format(date, 'HH:mm')}`;
      return format(date, 'MMM dd, yyyy HH:mm');
    } catch (error) {
      return 'Invalid date';
    }
  };

  const formatDateTime = (dateString) => {
    if (!dateString) return 'No date set';
    try {
      return format(new Date(dateString), 'MMM dd, yyyy HH:mm');
    } catch (error) {
      return 'Invalid date';
    }
  };

  const getTimeAgo = (dateString) => {
    if (!dateString) return null;
    try {
      return formatDistanceToNow(new Date(dateString), { addSuffix: true });
    } catch (error) {
      return null;
    }
  };

  const getTotalCalories = () => {
    return items.reduce((sum, item) => sum + (item.calorie || 0), 0);
  };

  return (
    <div className="food-items-container">
      {items.length > 0 && (
        <div className="consumption-summary">
          <h4>Total Calories: {getTotalCalories()}</h4>
        </div>
      )}
      
      {items.map((item) => {
        const timeAgo = getTimeAgo(item.createdAt);
        
        return (
          <div key={item.id} className="food-item">
            <div className="food-item-info">
              <h3>{item.name}</h3>
              {item.description && (
                <p style={{ marginBottom: '8px' }}>{item.description}</p>
              )}
              
              <div className="food-item-details">
                {item.calorie && (
                  <span className="detail-badge calorie-badge">
                    {item.calorie} calories
                  </span>
                )}
                {item.quantity && (
                  <span className="detail-badge quantity-badge">
                    {item.quantity}
                  </span>
                )}
              </div>
              
              <div className="food-item-meta">
                <span>Added: {formatDateTime(item.createdAt)}</span>
                {item.consumedDate && (
                  <>
                    <span>•</span>
                    <span>Consumed: {formatDate(item.consumedDate)}</span>
                  </>
                )}
                {timeAgo && (
                  <>
                    <span>•</span>
                    <span className="time-ago">{timeAgo}</span>
                  </>
                )}
              </div>
            </div>
            <div className="food-item-actions">
              <button
                onClick={() => onDelete(item.id)}
                className="btn btn-danger"
                title="Delete this food item"
              >
                Delete
              </button>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default FoodItemList;