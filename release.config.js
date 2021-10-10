module.exports = {
  branches: [
    {
      name: 'master'
    }
  ],
  tagFormat: 'V${version}',
  plugins: [
    [
      '@semantic-release/commit-analyzer',
      {
        preset: 'conventionalcommits',
        releaseRules: [
          { type: 'refactor', release: 'patch' },
          { scope: 'no-release', release: false }
        ]
      }
    ],
    [
      '@semantic-release/release-notes-generator',
      {
        preset: 'conventionalcommits',
        writerOpts: {
          groupBy: 'type',
          commitGroupsSort: function (a, b) {
            var commitGroupOrder = ['Features', 'Bug Fixes'];
            var aIndex = commitGroupOrder.indexOf(a.title);
            var bIndex = commitGroupOrder.indexOf(b.title);
            if (aIndex === -1) {
              return 1;
            } else if (bIndex === -1) {
              return -1;
            } else {
              return aIndex - bIndex;
            }
          },
          commitsSort: 'header'
        },
        presetConfig: {
          types: [
            { type: 'feat', section: 'Features' },
            { type: 'feature', section: 'Features' },
            { type: 'fix', section: 'Bug Fixes' },
            { type: 'perf', section: 'Performance Improvements' },
            { type: 'revert', section: 'Reverts' },
            { type: 'docs', section: 'Documentation' },
            { type: 'style', section: 'Styles' },
            { type: 'chore', section: 'Miscellaneous Chores', hidden: true },
            { type: 'refactor', section: 'Code Refactoring' },
            { type: 'test', section: 'Tests', hidden: true },
            { type: 'build', section: 'Build System', hidden: true },
            { type: 'ci', section: 'Continuous Integration', hidden: true }
          ]
        }
      }
    ],
    ['@semantic-release/npm'],
    ['@semantic-release/exec',
      {
        prepareCmd: 'npm run doctoc'
      }
    ],
    [
      '@semantic-release/changelog',
      {
        changelogFile: 'CHANGELOG.md'
      }
    ],
    [
      '@semantic-release/git',
      {
        message: 'chore(release): ${nextRelease.version}',
        assets: ['package.json', 'package-lock.json', 'CHANGELOG.md', 'readme.md']
      }
    ],
    [
      '@semantic-release/github',
      {
        successComment: false
      }
    ],
    [
      '@saithodev/semantic-release-backmerge',
      {
        branchName: 'dev',
        clearWorkspace: true
      }
    ]
  ]
};
