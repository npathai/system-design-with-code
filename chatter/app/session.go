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
