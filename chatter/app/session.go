package app

import (
	"github.com/npathai/chatter/model"
	"net/http"
)

func (app *App) CreateSession(session *model.Session) (*model.Session, *model.AppError) {
	session, err := app.Srv().Store.Session().Save(session)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Couldn't create session", http.StatusInternalServerError)
	}
	return session, nil
}

func (app *App) GetSessionByToken(token string) (*model.Session, *model.AppError) {
	session, err := app.Srv().Store.Session().GetSessionByToken(token)
	if err != nil {
		return nil, model.NewAppErrorWithStatus("Couldn't find session by token", http.StatusNotFound)
	}
	return session, nil
}
