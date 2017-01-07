package com.altla.vision.beacon.manager.presentation.presenter;

import android.support.annotation.IntRange;

import com.altla.vision.beacon.manager.R;
import com.altla.vision.beacon.manager.domain.usecase.FindNamespacesUseCase;
import com.altla.vision.beacon.manager.domain.usecase.FindProjectIdUseCase;
import com.altla.vision.beacon.manager.domain.usecase.SaveProjectIdUseCase;
import com.altla.vision.beacon.manager.presentation.presenter.mapper.NameSpaceModelMapper;
import com.altla.vision.beacon.manager.presentation.presenter.model.NameSpaceModel;
import com.altla.vision.beacon.manager.presentation.view.ProjectSwitchItemView;
import com.altla.vision.beacon.manager.presentation.view.SwitchProjectView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ProjectSwitchPresenter extends BasePresenter<SwitchProjectView> implements AuthFailure {

    @Inject
    FindProjectIdUseCase findProjectIdUseCase;

    @Inject
    FindNamespacesUseCase findNamespacesUseCase;

    @Inject
    SaveProjectIdUseCase saveProjectIdUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSwitchItemPresenter.class);

    private List<NameSpaceModel> nameSpaceModels = new ArrayList<>();

    private final NameSpaceModelMapper nameSpaceModelMapper = new NameSpaceModelMapper();

    @Inject
    public ProjectSwitchPresenter() {
    }

    @Override
    public void onResume() {
        getView().showTitle(R.string.title_switch_project);

        Subscription subscription = findProjectIdUseCase
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(projectId -> getView().showCurrentProject(projectId),
                        new DefaultAuthFailCallback(this) {

                            @Override
                            void onError(Throwable e) {
                                LOGGER.error("Failed to find current project id", e);
                                getView().showSnackBar(R.string.error_find_current_project_id);
                            }
                        });
        subscriptions.add(subscription);

        Subscription subscription1 = findNamespacesUseCase
                .execute()
                .flatMapObservable(namespacesEntity -> Observable.from(namespacesEntity.namespaces))
                .map(nameSpaceModelMapper::map)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nameSpaceModels -> {
                    this.nameSpaceModels.clear();
                    this.nameSpaceModels.addAll(nameSpaceModels);
                    getView().updateItems();
                }, new DefaultAuthFailCallback(this) {

                    @Override
                    void onError(Throwable e) {
                        LOGGER.error("Failed to find namespaces", e);
                        getView().showSnackBar(R.string.error_find);
                    }
                });
        subscriptions.add(subscription1);
    }

    public void onCreateItemView(ProjectSwitchItemView projectSwitchItemView) {
        ProjectSwitchItemPresenter projectSwitchItemPresenter = new ProjectSwitchItemPresenter();
        projectSwitchItemPresenter.onCreateItemView(projectSwitchItemView);
        projectSwitchItemView.setItemPresenter(projectSwitchItemPresenter);
    }

    public int getItemCount() {
        return nameSpaceModels.size();
    }

    @Override
    public void refreshToken() {
        getView().refreshToken();
    }

    public final class ProjectSwitchItemPresenter extends BaseItemPresenter<ProjectSwitchItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(nameSpaceModels.get(position));
        }

        public void onItemClick(NameSpaceModel model) {
            Subscription subscription = saveProjectIdUseCase
                    .execute(model.projectId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> getView().showCurrentProject(model.projectId),
                            new DefaultAuthFailCallback(ProjectSwitchPresenter.this) {
                                @Override
                                void onError(Throwable e) {
                                    LOGGER.error("Failed to switch project", e);
                                }
                            });
            subscriptions.add(subscription);
        }
    }
}
