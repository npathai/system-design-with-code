package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) CreateUser(user *model.User) (*model.User, *model.AppError) {
	err := app.validateUser(user)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Invalid user", http.StatusBadRequest)
	}
	rUser, err := app.Srv().Store.User().Save(user)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Error creating the user", http.StatusInternalServerError)
	}

	return rUser, nil
}

func (app *App) GetAllUsers() ([]*model.User, *model.AppError) {
	users, err := app.Srv().Store.User().GetAllUsers()
	if err != nil {
		return nil, model.NewAppError("Cannot retrieve users")
	}
	return users, nil
}

func (app *App) validateUser(user *model.User) error {
	return nil
}

func (app *App) GetUser(userId string) (*model.User, *model.AppError) {
	user, err := app.Srv().Store.User().Get(userId)
	if err != nil {

	}
	return user, nil
}