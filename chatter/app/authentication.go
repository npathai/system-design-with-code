package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) authenticateUser(user *model.User, password string) (*model.User, *model.AppError) {

	if err := app.CheckPasswordAndCriteria(user, password); err != nil {
		err.StatusCode = http.StatusUnauthorized
		return nil, err
	}

	return user, nil
}


func (a *App) CheckPasswordAndCriteria(user *model.User, password string) *model.AppError {
	return nil
}