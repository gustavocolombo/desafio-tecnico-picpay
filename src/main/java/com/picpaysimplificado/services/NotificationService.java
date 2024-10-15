package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;

public interface NotificationService {
  void sendNotification(User user, String message) throws Exception;
}
